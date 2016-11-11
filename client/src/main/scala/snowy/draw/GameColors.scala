package snowy.draw
import snowy.playfield.Color

object GameColors {
  val lineColors = Color(100, 100, 100)
  val clearColor = Color(255, 255, 255)
  val snowball   = Color(208, 242, 237).toString

  object Tree {
    val leaves = Color(94, 153, 105).toString
    val trunk  = Color(56, 85, 58).toString
  }

  object Sled {
    val bodyGreen = Color(120, 201, 44).toString
    val bodyRed   = Color(241, 78, 84).toString
  }
}
