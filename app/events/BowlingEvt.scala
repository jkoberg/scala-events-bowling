package events

import julienrf.variants.Variants


sealed trait BowlingEvt
case class PlayerRegistered(name:String)  extends BowlingEvt
case class PinsDropped(pins:Set[Int])     extends BowlingEvt
case class FrameEnded(pins:Set[Int])      extends BowlingEvt
case object BallStranded                  extends BowlingEvt
case object BallRecovered                 extends BowlingEvt
case class GameOver(frameScores:Seq[Int]) extends BowlingEvt

object BowlingEvt {
  implicit val formatsJson = Variants.format[BowlingEvt]("Event")
}