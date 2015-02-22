package events


import scala.concurrent.Future

trait BowlingServices {
  def resetLane(n:Int): Future[Unit]
  def notifyPlayer(name:String, message:String): Future[Unit]
}
