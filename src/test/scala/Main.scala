import cats.effect.{IO, IOApp, Resource}
import cats.implicits._
import scala.concurrent.ExecutionContext

import munit.CatsEffectSuite
import munit.Clue.generate

class MainTest extends munit.CatsEffectSuite {
  test(
    "13533571 = 1709 x 7919"
  ) {
    val pqTest = (1709, 7919)
    val target = 13533571
    val targetDivLim = 10000
    for {
      pq <- Factorer.threadPool.use { ec =>
        Factorer
          .findFactors(target, targetDivLim)
          .evalOn(ec)
      }
    } yield assertEquals(pqTest, pq)
  }
  
  test(
    "1993 = 1993 x 1"
  ) {
    val pqTest = (1, 1993)
    val target = 1993
    val targetDivLim = 2000
    for {
      pq <- Factorer.threadPool.use { ec =>
        Factorer
          .findFactors(target, targetDivLim)
          .evalOn(ec)
      }
    } yield assertEquals(pqTest, pq)
  }

}
