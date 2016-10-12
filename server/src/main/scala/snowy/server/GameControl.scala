package snowy.server

import scala.collection.mutable
import scala.concurrent.duration._
import scala.math.min
import snowy.GameClientProtocol._
import snowy.GameConstants
import snowy.GameConstants.Friction.slowButtonFriction
import snowy.GameConstants.{Bullet, _}
import snowy.GameServerProtocol._
import snowy.collision.{SledSnowball, SledTree}
import snowy.playfield.GameMotion.{moveSleds, moveSnowballs}
import snowy.playfield._
import socketserve.{AppController, AppHostApi, ConnectionId}
import upickle.default._
import vector.Vec2d

class GameControl(api: AppHostApi) extends AppController with GameState {
  val tickDelta = 20 milliseconds
  val turnDelta = (math.Pi / turnTime) * (tickDelta.toMillis / 1000.0)
  val connections = mutable.Map[ConnectionId, ClientConnection]()

  api.tick(tickDelta) {
    connections.values.foreach(_.refreshTiming())
    gameTurn()
  }

  /** Called to update game state on a regular timer */
  private def gameTurn(): Unit = {
    val deltaSeconds = nextTimeSlice()
    recoverHealth(deltaSeconds)
    recoverPushEnergy(deltaSeconds)
    applyCommands(deltaSeconds)
    expireSnowballs()
    sleds = moveSleds(sleds, deltaSeconds)
    snowballs = moveSnowballs(snowballs, deltaSeconds)
    checkCollisions()
    reapDead()
    updateScore()
    currentState() foreach {
      case (id, state) => api.send(write(state), id)
    }
    val scores = users.values.map { user => Score(user.name, user.score) }.toSeq
    users.foreach {
      case (id, user) =>
        val scoreboard = Scoreboard(user.score, scores)
      // api.send(write[GameClientMessage](scoreboard), id)
    }
  }

  /** a new player has connected */
  override def open(id: ConnectionId): Unit = {
    connections(id) = new ClientConnection(id, api)
    val clientPlayfield = Playfield(playfield.x.toInt, playfield.y.toInt)
    api.send(write(clientPlayfield), id)
    api.send(write(Trees(trees.toSeq)), id)
  }

  /** Called when a connection is dropped */
  override def gone(id: ConnectionId): Unit = {
    sledMap.get(id).foreach { sledId =>
      sledId.sled.remove()
    }
    users.remove(id)
    commands.commands.remove(id)
    connections.remove(id)
  }

  /** received a client message */
  override def message(id: ConnectionId, msg: String): Unit = {
    read[GameServerMessage](msg) match {
      case Join(name)         => userJoin(id, name)
      case TurretAngle(angle) => rotateTurret(id, angle)
      case Shoot              => shootSnowball(id)
      case Start(cmd)         => commands.startCommand(id, cmd)
      case Stop(cmd)          => commands.stopCommand(id, cmd)
      case Pong               => connections(id).pongReceived()
    }
  }

  private def newRandomSled(userName: String): Sled = {
    // TODO what if sled is initialized atop a tree?
    Sled(userName = userName, pos=randomSpot(), size = 35, speed = Vec2d(0, 0),
      rotation = downhillRotation, turretRotation = downhillRotation)
  }

  /** Called when a user sends her name and starts in the game */
  private def userJoin(id: ConnectionId, userName: String): Unit = {
    users(id) = User(userName)
    val sled = newRandomSled(userName)
    sleds = sleds.add(sled)
    sledMap(id) = sled.id
  }

  /** Rotate the turret on a sled */
  private def rotateTurret(id: ConnectionId, angle: Double): Unit = {
    modifySled(id) { sled =>
      sled.copy(turretRotation = angle)
    }
  }

  private def modifySled(id: ConnectionId)(fn: Sled => Sled): Unit = {
    sledMap.get(id).foreach { sledId =>
      sleds = sleds.replaceById(sledId) { sled =>
        fn(sled)
      }
    }
  }

  private def shootSnowball(id: ConnectionId): Unit = {
    modifySled(id) { sled =>
      val direction = Vec2d.fromRotation(-sled.turretRotation)
      // TODO use GameConstants for these magic numbers
      val ball = Snowball(
        ownerId = sled.id,
        pos = sled.pos + direction * 35,
        size = GameConstants.Bullet.size,
        speed = (sled.speed / 50) + (direction * 10),
        spawned = System.currentTimeMillis()
      )
      snowballs = snowballs.add(ball)

      val recoilForce = direction * -Bullet.recoil
      val speed = sled.speed + recoilForce
      sled.copy(speed = speed)
    }
  }

  /** Rotate a sled.
    *
    * @param rotate rotation in radians from current position. */
  private def turnSled(sled: Sled, rotate: Double): Sled = {
    // TODO limit turn rate to e.g. 1 turn / 50msec to prevent cheating by custom clients?
    val max = math.Pi * 2
    val min = -math.Pi * 2
    val rotation = sled.rotation + rotate
    val wrappedRotation =
      if (rotation > max) rotation - max
      else if (rotation < min) rotation - min
      else rotation
    sled.copy(rotation = wrappedRotation)
  }

  /** apply any pending but not yet cancelled commands from user actions,
    * e.g. turning or slowing */
  def applyCommands(deltaSeconds: Double): Unit = {
    val slow = new InlineForce(-slowButtonFriction * deltaSeconds)
    val pushForceNow = PushEnergy.force * deltaSeconds
    val pushEffort = deltaSeconds / PushEnergy.maxTime
    val push = new InlineForce(pushForceNow)
    commands.foreachCommand { (id, command) =>
      modifySled(id) { sled =>
        command match {
          case Left  => turnSled(sled, turnDelta)
          case Right => turnSled(sled, -turnDelta)
          case Slow  => sled.copy(speed = slow(sled.speed))
          case Push  => pushSled(sled, pushForceNow, push, pushEffort)
        }
      }
    }
  }

  /** apply a push to a sled */
  private def pushSled(sled: Sled, pushForceNow: Double, push: InlineForce, effort: Double)
  : Sled = {
    if (effort < sled.pushEnergy) {
      val speed =
        if (sled.speed.zero) Vec2d.fromRotation(sled.rotation) * pushForceNow
        else push(sled.speed)
      val energy = sled.pushEnergy - effort
      sled.copy(speed = speed, pushEnergy = energy)
    } else {
      sled
    }
  }

  /** slowly recover some health points */
  private def recoverHealth(deltaSeconds: Double): Unit = {
    val deltaHealth = deltaSeconds / Health.recoveryTime
    sleds = sleds.replaceItems { sled =>
      val newHealth = min(1.0, sled.health + deltaHealth)
      sled.copy(health = newHealth)
    }
  }

  /** slowly recover some push energy */
  private def recoverPushEnergy(deltaSeconds: Double): Unit = {
    val deltaEnergy = deltaSeconds / PushEnergy.recoveryTime
    sleds = sleds.replaceItems { sled =>
      val energy = min(1.0, sled.pushEnergy + deltaEnergy)
      sled.copy(pushEnergy = energy)
    }
  }


  def expireSnowballs(): Unit = {
    val now = System.currentTimeMillis()
    snowballs = snowballs.removeMatchingItems { snowball =>
      now > snowball.spawned + Bullet.lifetime
    }
  }

  /** Notify clients whose sleds have been killed, and remove them from the game */
  private def reapDead(): Unit = {
    val reap = sleds.items.collect {
      case sled if (sled.health <= 0) => sled.connectionId -> sled
    }
    reap.foreach { case (connectionId, sled) =>
      api.send(write(Died), connectionId)
      sleds = sleds.remove(sled)
    }
  }


  /** update the score based on sled travel distance */
  private def updateScore(): Unit = {
    sleds.items.foreach { sled =>
      val id = sled.connectionId
      users.get(id) match {
        case Some(user) =>
          val travelPoints = sled.distanceTraveled * Points.travel
          users(id) = user.copy(score = user.score + travelPoints)
        case None       =>
          println(s"updateScore. no user found for id: $id")
      }
    }
  }

  /** check for collisions between the sled and trees or snowballs */
  private def checkCollisions(): Unit = {
    import snowy.collision.GameCollide.snowballTrees
    val collisions = sleds.items.map { sled =>
      val sledPostSnowballs =
        SledSnowball.collide(sled, snowballs).map {case (newSled, newBalls) =>
          snowballs = newBalls
          newSled
        }.getOrElse(sled)

      val sledPostTrees = SledTree.collide(sledPostSnowballs, trees)
      sledPostTrees.map{newSled => (sled, newSled)}
    }

    collisions.flatten.foreach { case (oldSled, newSled) =>
      sleds = sleds.remove(oldSled).add(newSled)
    }

    snowballs = snowballs.removeMatchingItems(snowballTrees(_, trees))
  }

  /** Advance to the next game simulation state
    *
    * @return the time since the last time slice, in seconds
    */
  private def nextTimeSlice(): Double = {
    val currentTime = System.currentTimeMillis()
    val deltaSeconds = (currentTime - lastTime) / 1000.0
    lastTime = currentTime
    deltaSeconds
  }


}
