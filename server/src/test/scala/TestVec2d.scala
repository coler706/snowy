import org.scalacheck.Prop._
import org.scalacheck._
import org.scalacheck.Gen._

object TestVec2d extends Properties("Vec2d") {
  def angleZero(d:Double):Prop = {
    val a = Vec2d(d,d)
    val b = Vec2d(d,d)
    a.angle(b) ?= 0
  }

  property("angle 0") = forAll(chooseNum(-1e100, 1e100)) {
    angleZero _
  }

}
