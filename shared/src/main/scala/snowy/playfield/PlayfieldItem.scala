package snowy.playfield
import vector.Vec2d

/** A collidable object on the playfield */
trait PlayfieldItem[A <: PlayfieldItem[A]] extends Bounds { this: A =>
  var health: Double

  private var internalPosition = Vec2d.zero

  val id: PlayId[A] = PlayId.nextId[A]()

  def position: Vec2d = internalPosition

  def position_=(pos: Vec2d)(implicit tracker: PlayfieldTracker[A]): Unit = {
    tracker.remove(this)
    internalPosition = pos
    tracker.add(this)
  }

  def setInitialPosition(pos:Vec2d)(implicit tracker: PlayfieldTracker[A]):Unit = {
    internalPosition = pos
    tracker.add(this)
  }

  def canEqual(a: Any): Boolean = a.isInstanceOf[PlayfieldItem[A]]

  override def hashCode(): Int = id.id

  override def equals(that: Any): Boolean = that match {
    case that: PlayfieldItem[A] => canEqual(that) && id == that.id
    case _                      => false
  }

  /** reduce impact by this factor in collisions */
  def armor: Double = 1.0

  def impactDamage: Double = 1.0

  override def toString() = {
    val named  = getClass.getSimpleName
    s"$named[${id.id}]"
  }
}

/** An object with a bounding box */
trait Bounds {
  def boundingBox: Rect
}