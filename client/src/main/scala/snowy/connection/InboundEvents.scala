package snowy.connection

import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import boopickle.Default._
import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import snowy.client.{DrawState2, LoginScreen, LoginScreen2}
import snowy.connection.GameState._
import snowy.playfield.Picklers._
import snowy.playfield.{SkiColor, SledKind}
import vector.Vec2d

class InboundEvents(socket: NetworkSocket,
                    sendMessage: (GameServerMessage) => Unit,
                    name: String,
                    kind: SledKind,
                    color: SkiColor) {

  def arrayBufferMessage(arrayBuffer: ArrayBuffer): Unit = {
    val byteBuffer = TypedArrayBuffer.wrap(arrayBuffer)
    val message    = Unpickle[GameClientMessage].fromBytes(byteBuffer)
    handleMessage(message)
  }

  socket.onOpen { _ =>
    GameState.serverGameClock = Some(new ServerGameClock(sendMessage))
    sendMessage(Join(name, kind, color))
  }

  socket.onError { event =>
    console.log(s"Failed: code: $event")
  }

  socket.onClose { _ =>
    console.log(s"socket closed ")
    LoginScreen.startPanel()
  }

  socket.onMessage { event =>
    event.data match {
      case arrayBuffer: ArrayBuffer => arrayBufferMessage(arrayBuffer)
      case x                        => console.log(s"unexpected message received: $x")
    }
  }

  private def handleMessage(message: GameClientMessage): Unit = {
    message match {
      case state: State                => receivedState(state)
      case Playfield(width, height)    => gPlayField = Vec2d(width, height)
      case trees: Trees                => serverTrees = serverTrees.addItems(trees.trees)
      case Died                        => LoginScreen2.rejoinPanel()
      case Ping                        => sendMessage(Pong)
      case ClientPong                  => // currently used only by the load test client
      case GameTime(time, oneWayDelay) => updateClock(time, oneWayDelay)
      case MySled(sledId)              => mySledId = Some(sledId)
      case SnowballDeaths(balls)       => println(s"died. snowballs: $balls") // TODO use me
      case SledDeaths(sleds)           => DrawState2.removeSleds(sleds)
      case newScoreboard: Scoreboard   => scoreboard = newScoreboard
    }
  }

  private def updateClock(time: Long, oneWayDelay: Int): Unit = {
    GameState.serverGameClock.foreach(_.updateClock(time, oneWayDelay))
  }
}
