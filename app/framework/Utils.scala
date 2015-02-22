package framework

import scala.concurrent.{ExecutionContext, Future}

import scala.concurrent.ExecutionContext.Implicits.global

object Utils {

  implicit class FExt[A, B](f: Future[A]) {
    def thenKeep (that: B): Future[B] = for {_ <- f} yield that
  }

}