package events

import events.Scoring.{OpenFrame, ClosedFrame}
import framework.{Emit, Reject, AggregateRoot}
import framework.Utils._
import julienrf.variants.Variants
import scala.concurrent.Future


sealed trait BowlingState extends AggregateRoot[BowlingEvt, BowlingCmd, BowlingServices]

object BowlingState {
  implicit val formatsJson = Variants.format[BowlingState]("State")
  val initialState = GameFinished(None)
}


case class GameFinished(lastGameScores:Option[(String, Seq[Int])]) extends BowlingState {
  override def transitions = {
    case PlayerRegistered(name) => WaitingForPlayer(name)
    case BallStranded =>           WaitingForStaff(this)
  }
  override def commands: DoesCmd = svcs => {
    case RegisterPlayer(name) =>
      svcs.resetLane(0) thenKeep
      Emit(
        PlayerRegistered(name)
      ) onPersist {
        svcs.notifyPlayer(name)
      }

    case RollBall(_) =>
      Emit(BallStranded).asFuture
  }
}

case class WaitingForPlayer(name:String, pinsUp:Set[Int]=(1 to 10).toSet, rollRecord:Seq[Int]=Seq.empty) extends BowlingState {
  import RandomUtils.OnIterables

  override def commands: DoesCmd = svcs => {
    case RollBall(skill) =>
      val dropped = pinsUp.chooseWithP(skill).toSet
      val newCmd = PreciseRoll(dropped)
      commands(svcs)(newCmd)

    case PreciseRoll(dropped) =>
      val event = Scoring.score((rollRecord :+ dropped.size).toList) match {
        case ClosedFrame(scored)         => if scored.size == 10 then GameOver(scored) else FrameEnded(pinsUp -- dropped)
        case OpenFrame(scored, unscored) => PinsDropped(dropped)
      }
      Emit(event) onPersist { es =>
        svcs.resetLane(0) thenKeep es.allDo(svcs notifyPlayer (name, s"$_"))
      }
  }

  override def transitions = {
    case PinsDropped(s)      => WaitingForPlayer(name, pinsUp -- s, rollRecord :+ s.size)
    case FrameEnded(dropped) => WaitingForPlayer(name)
    case GameOver(scored)    => GameFinished(Some((name,scored)))
  }
}


case class WaitingForStaff(before:BowlingState) extends BowlingState {
  override def transitions = {
    case BallRecovered => before
  }
  override def commands: DoesCmd = svcs => {
    case RecoverBall => Emit(BallRecovered).asFuture
  }

}