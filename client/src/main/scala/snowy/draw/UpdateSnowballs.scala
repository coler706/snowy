package snowy.draw

import minithree.THREE.Object3D
import minithree.raw.Vector3
import snowy.GameConstants
import snowy.client.DrawState2
import snowy.client.DrawState2.{Bodies, Groups, Meshes}
import snowy.playfield.PlayId.BallId
import snowy.playfield.Snowball

object UpdateSnowballs {
  def updateCsnowballs(snowballs: Set[Snowball]): Unit = {
    snowballs.foreach { snowball1 =>
      var idExists = false
      Groups.csnowballs.children.zipWithIndex.foreach {
        case (aSnowball, index) =>
          if (aSnowball.name == snowball1.id.id.toString) {
            idExists = true
            val csnowball = Groups.csnowballs.children(index)
            val newPos = DrawState2.transformPositionMod(
              new Vector3(snowball1._position.x, 0, snowball1._position.y),
              Bodies.sled.position,
              new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
            )
            csnowball.position.x = newPos.x
            csnowball.position.z = newPos.z
          }
      }
      if (!idExists) {
        addSnowball(snowball1)
      }
    }
  }

  def addSnowball(snowball: Snowball): Unit = {
    val newSnowball: Object3D = Meshes.snowball.clone()
    newSnowball.scale.set(
      snowball.radius,
      snowball.radius,
      snowball.radius
    )
    val newPos = DrawState2.transformPositionMod(
      new Vector3(snowball._position.x, 0, snowball._position.y),
      Bodies.sled.position,
      new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
    )
    newSnowball.position.x = newPos.x
    newSnowball.position.z = newPos.z
    newSnowball.name = snowball.id.id.toString
    Groups.csnowballs.add(newSnowball)
  }

  def removeSnowballs(deaths: Seq[BallId]): Unit = {
    val ids = deaths.map(_.id.toString)
    Groups.csnowballs.children = Groups.csnowballs.children.filter { snowball =>
      !ids.contains(snowball.name)
    }
  }
}
