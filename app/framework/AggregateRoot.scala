package framework

import scala.concurrent.Future

trait AggregateRoot[E,C,S] {
  type Evt = E
  type Cmd = C
  type Svcs = S
  
  import Utils.FExt._
  
  type AppliesEvt = PartialFunction[Evt, AggregateRoot[Evt,Cmd,Svcs]]
  def transitions: AppliesEvt
  def applyEvent: AppliesEvt = transitions orElse defaultTransitions
  def defaultTransitions: AppliesEvt = {
    case _ => this
  }


  type DoesCmd = (Svcs) => PartialFunction[Cmd, Future[Outcome[Evt]]]
  def commands: DoesCmd
  def tryCommand: DoesCmd = (svcs) => commands(svcs) orElse defaultCommands(svcs)
  def defaultCommands: DoesCmd = (svcs) => {
    case cmd => Reject(s"command $cmd invalid in state $this").asFuture
  }

}
