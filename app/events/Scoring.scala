package events

import scala.annotation.tailrec

object Scoring {
  sealed trait FrameResult
  case class ClosedFrame(scored:Seq[Int]) extends FrameResult
  case class OpenFrame(scored:Seq[Int], remaining:Seq[Int]) extends FrameResult

  @tailrec
  final def score(rolls:List[Int], scored:Seq[Int]=Seq.empty) : FrameResult =
    rolls match {
      case r1 :: (more @ b1 :: b2  :: _ ) if r1 == 10        => score(more, scored +: (r1 + b1 + b2))
      case r1 :: r2 :: (more @ b1 :: _ )  if r1 + r2  == 10  => score(more, scored +: (r1 + r2 + b1))
      case r1 :: r2 :: (more @ _ )        if r1 + r2 < 10    => score(more, scored +: (r1 + r2))
      case Nil  => ClosedFrame(scored)
      case more => OpenFrame(scored, more)
    }
}
