package events

import util.Random.nextDouble

object RandomUtils {
  val randomNumbers = Stream.continually(nextDouble())

  implicit class OnIterables[A](self: Iterable[A]) {
    def chooseWithP(p: Double): Iterable[A] =
      self.zip(randomNumbers).filter(_._2 < p).map(_._1)
  }
}
