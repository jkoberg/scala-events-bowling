package events

object RandomUtils {
  val randomNumbers = Stream.continually(util.Random.nextDouble())

  implicit class OnIterables[A](self: Iterable[A]) {
    def chooseWithP(p: Double): Iterable[A] =
      self.zip(randomNumbers).filter(_._2 < p).map(_._1)
  }
}


class RandomUtilsTests {
  def example(): Unit = {
    import RandomUtils.OnIterables
    val someIterable = Seq("hello", "there", "this", "is", "a", "sequence", "of", "words")
    val withP = someIterable chooseWithP 0.10
    printf(s"$withP")

  }

}