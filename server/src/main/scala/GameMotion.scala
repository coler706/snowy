/** Moving objects in each game time slice */
trait GameMotion {
  self: GameControl =>

  val maxSpeed = 1000 // pixels / second

  /** update sleds and snowballs speeds and positions */
  protected def moveStuff(deltaSeconds: Double): Unit = {
    applyGravity(deltaSeconds)
    skidSleds(deltaSeconds)
    frictionSlow(deltaSeconds)
    moveObjects(deltaSeconds)
    checkCollisions()
  }

  /** rotate the sleds towards the direction of their skis incrementally
    * to account for skidding */
  def skidSleds(deltaSeconds: Double):Unit = {
    val skidTime = Skid.skidTime(deltaSeconds)
    mapSleds {sled =>
      val newSpeed = Skid.skid(sled.speed, sled.rotation, skidTime)
      sled.copy(speed = newSpeed)
    }
  }

  /** Increase the speed of sleds due to gravity */
  private def applyGravity(deltaSeconds: Double): Unit = {
    val gravity = -250.0 // pixels / second / second
    val gravityFactor = gravity * deltaSeconds
    mapSleds { sled =>
      val rotation = sled.rotation
        if (sled.rotation > downhillRotation + math.Pi/2) sled.rotation - math.Pi
        else if (sled.rotation < downhillRotation - math.Pi/2) sled.rotation + math.Pi
        else sled.rotation
      val gravityLength = math.cos(rotation) * gravityFactor // +speed in direction of travel
      val gravitySpeedV = Vec2d.fromRotation(rotation) * gravityLength
      val newSpeedV = sled.speed + gravitySpeedV
      val adjustedSpeedV =
        if (newSpeedV.length <= maxSpeed) newSpeedV
        else newSpeedV.unit * maxSpeed

      // println(s"gravity:  sled.rotation: ${sled.rotation}  rotation: $rotation  speed: ${sled.speed}  adjustedSpeedV: $adjustedSpeedV")
      sled.copy(speed = adjustedSpeedV)
    }
  }

  /** Slow sleds based on friction */
  private def frictionSlow(deltaSeconds:Double): Unit =  {
    import math._
    // friction is min when skis are aligned with direction, max when skis are at 90 degrees
    val friction = 500.0 // pixels / second / second
    val minFriction = 50.0 * deltaSeconds
    val frictionFactor = friction * deltaSeconds
    val brakeSteepness = .8  // higher means braking effect peaks narrowly when skis are near 90 to travel
    mapSleds { sled =>
      val speed = sled.speed.length
      speed match {
        case 0 => sled
        case _ =>
          val direction = sled.speed.unit
          val rotation = Vec2d.fromRotation(sled.rotation)
          val angleSkiToTravel = direction.angle(rotation)
          /*
          val angleSkiToTravel =
            if (rawAngleSkiToTravel > math.Pi / 2) rawAngleSkiToTravel - math.Pi
            else if (rawAngleSkiToTravel < -math.Pi / 2) rawAngleSkiToTravel + math.Pi
            else rawAngleSkiToTravel
            */
          val rotationFactor = pow(abs(sin(angleSkiToTravel)), brakeSteepness)
          val sledFriction = minFriction + frictionFactor * rotationFactor // -speed in direction of travel

          val adjustedFriction = min(sledFriction, speed)
          val newSpeed = direction * (speed - adjustedFriction)
//          println(s"speed: $speed  direction: $direction  rotation: $rotation")
//          println(s"angleSkiToTravel: $angleSkiToTravel  rotationFactor:$rotationFactor  sledFriction: $sledFriction  newSpeed: $newSpeed")
          sled.copy(speed = newSpeed)
      }
    }
  }

  /** Run a function that replaces each sled */
  private def mapSleds(fn: SledState => SledState): Unit = {
    sleds = sleds.map { case (id, sled) =>
      id -> fn(sled)
    }
  }

  /** constrain a position to be within the playfield */
  def wrapInPlayfield(pos: Vec2d): Vec2d = {
    Vec2d(
      wrapBorder(pos.x, playField.width),
      wrapBorder(pos.y, playField.height)
    )
  }

  /** Constrain a value between 0 and a max value.
    * values past one border of the range are wrapped to the other side
    *
    * @return the wrapped value */
  private def wrapBorder(value: Double, max: Double): Double = {
    val result =
      if (value >= max * 2.0)
        max
      else if (value >= max)
        value - max
      else if (value < max * -2.0)
        0
      else if (value < 0)
        max + value
      else
        value
    if (result < 0 || result >= max) {
      println(s"wrapBorder error: $value < $max   $result")
    }
    result
  }


  /** move movable objects to their new location for this time period */
  private def moveObjects(deltaSeconds: Double): Unit = {
    mapSleds { sled =>
      val moved = sled.pos + (sled.speed * deltaSeconds)
      val wrapped = wrapInPlayfield(moved)
      sled.copy(pos = wrapped)
    }
  }

  private def checkCollisions(): Unit = {
    // TODO
  }

}
