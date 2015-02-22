package framework

import scala.concurrent.Future

class Sequenceable[+A] extends Seq[A] {
  def sequence(f:A=>Future[Any]) = {
    Future.traverse(this)(f)
  }
}

sealed trait Outcome[+E] {
  def asFuture = Future.successful(this)
}
case class EmitAnd[+E](evts:E*)(block: =>Future[Any]) extends Outcome[E]
case class Reject[+E](why:String)                     extends Outcome[E]
case class Emit[+E](evts:E*) extends Outcome[E] {
  def onPersist(block: => Future[Any]) = EmitAnd(evts:_*)(block)
  def onPersist(block: Sequenceable[E] => Future[Any]) = EmitAnd(evts:_*)(block(evts))
}
