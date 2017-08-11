package snowy.server.rewards

import snowy.GameConstants.Points.minPoints
import snowy.GameConstants.{absoluteMaxHealth, absoluteMaxSpeed, kingHealthBonus}
import snowy.server.{ServerSled, User}
import snowy.util.ClosestTable

sealed trait RewardTableEntry

trait TableToReward {
  def toSingleReward(repeat: Int): SingleReward
}

case class SpeedTable(amounts: Int*) extends TableToReward with RewardTableEntry {
  private val table = new ClosestTable[Int](0, amounts: _*)

  override def toSingleReward(repeat: Int): SingleReward = {
    val speed = table.get(repeat)
    MaxSpeedBonus(speed)
  }
}

trait SingleReward extends RewardTableEntry {
  def applyToSled(serverSled: ServerSled)
}

case class Score(amount: Int) extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.user.score += amount
  }
}

case class AddScoreFrom(fromUser:User, multiple:Double) extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val proposedScore = serverSled.user.score + fromUser.score * multiple
    serverSled.user.score  = math.max(minPoints, proposedScore)
  }
}

case class MaxSpeedBonus(amount: Int) extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val current = serverSled.sled.maxSpeed
    serverSled.sled.maxSpeed = math.min(current + amount, absoluteMaxSpeed)
  }
}

case class MaxHealthBonus(amount: Double) extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val current = serverSled.sled.maxHealth
    serverSled.sled.maxHealth = math.min(current + amount, absoluteMaxHealth)
  }
}

case object OnFire extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.sled.maxHealth += kingHealthBonus
  }
}

case object NoMoreFire extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.sled.maxHealth -= 5
  }
}

