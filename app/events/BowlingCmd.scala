package events

import julienrf.variants.Variants

import scala.concurrent.Future

sealed trait BowlingCmd
case class RegisterPlayer(name:String) extends BowlingCmd
case class RollBall(skill:Double)      extends BowlingCmd
case class PreciseRoll(pins:Set[Int])  extends BowlingCmd
case object RecoverBall                extends BowlingCmd

object BowlingCmd {
  implicit val jsonFormat = Variants.format[BowlingCmd]("Command")
}