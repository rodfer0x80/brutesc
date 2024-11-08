import cats.effect.{IO, IOApp, Resource}
import cats.implicits._
import scala.concurrent.ExecutionContext

import munit.CatsEffectSuite
import munit.Clue.generate

class MainTest extends munit.CatsEffectSuite {
  test(
    "13533571 = 1709 x 7919" 
  ) {
    val pqTest = (7919, 1709)
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

}
