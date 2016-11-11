package snowy.playfield

import vector.Vec2d
import snowy.GameConstants.downhillRotation

object Sled {
  val dummy = new Sled(userName = "dummy", _position = Vec2d.zero)

  def apply(userName: String): Sled = {
    new Sled(userName = userName, _position = Vec2d.zero)
  }

  def apply(userName: String, initialPosition: Vec2d, kind: SledKind): Sled = {
    new Sled(userName = userName, _position = initialPosition, kind = kind)
  }
}
/* rotation in radians, 0 points down the screen, towards larger Y values.
 * speed in pixels / second
 */
case class Sled(id: PlayId[Sled] = PlayfieldObject.nextId(),
                userName: String,
                var _position: Vec2d,
                var speed: Vec2d = Vec2d.zero,
                kind: SledKind = BasicSled,
                var rotation: Double = downhillRotation,
                var health: Double = 1,
                impactDamage: Double = 1,
                var turretRotation: Double = downhillRotation,
                var pushEnergy: Double = 1,
                var lastShotTime: Long = 0)
    extends CircularObject {
  type MyType = Sled

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Sled]

  override def copyWithUpdatedPos(newPos: Vec2d): Sled = this.copy(_position = newPos)

  def radius: Double = kind.radius

  /** acceleration in pixels / second / second */
  def gravity: Double = kind.gravity

  /** max speed of sled in pixels per second */
  def maxSpeed: Double = kind.maxSpeed

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = kind.minRechargeTime

  /** factor increasing or decreasing damage from being hit with a snowball */
  def bulletImpactFactor: Double = kind.bulletImpactFactor

  /** speed of bullet in pixels/sec */
  def bulletSpeed: Int = kind.bulletSpeed

  /** radius in pixels */
  def bulletRadius: Int = kind.bulletRadius

  /** acceleration due to recoil in pixels/sec/sec */
  def bulletRecoil: Int = kind.bulletRecoil

  /** bullet begins its flight this pixel offset from the sled center
    * if the sled is shooting straight up */
  def bulletLaunchPosition: Vec2d = kind.bulletLaunchPosition

  /** launch angle rotation from turret direction, e.g. for rear facing cannon */
  def bulletLaunchAngle: Double = kind.bulletLaunchAngle

  /** Initial health of a bullet. Bullets with enough health survive collisions and rebound */
  def bulletHealth: Double = kind.bulletHealth

  /** health of this sled. If it falls to zero, the sled dies. */
  def maxHealth: Double = kind.maxHealth

  /** time in seconds to recover 1 full point of health */
  def healthRecoveryTime: Double = kind.healthRecoveryTime

  /** health as a value between zero and one */
  def healthPercent: Double = health / kind.maxHealth

  /** deliver this amount of damage on collision with another sled at full speed */
  def maxImpactDamage: Double = kind.maxImpactDamage

  /** reduce impact by this factor in sled/sled collisions */
  override def armor: Double = kind.armor

  /** sleds heavier than 1.0 accelerate and decelerate more slowly */
  override def mass: Double = kind.mass
}