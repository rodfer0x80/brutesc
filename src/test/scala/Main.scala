import cats.effect.{IO, IOApp, Resource}
import cats.implicits._
import scala.concurrent.ExecutionContext

import munit.CatsEffectSuite
import munit.Clue.generate

class MainTest extends munit.CatsEffectSuite {
  test(
    "cats-effects: 13533571 = 1709 x 7919"
  ) {
    val pqTest = (1709, 7919)
    val target = 13533571
    val targetDivLim = 10000
    for {
      pq <- Factorer.threadPool.use { ec =>
        Factorer
          .findFactorsCats(target)
          .evalOn(ec)
      }
    } yield assertEquals(pqTest, pq)
  }

  test(
    "cats-effects: 1993 = 1993 x 1"
  ) {
    val pqTest = (1, 1993)
    val target = 1993
    val targetDivLim = 2000
    for {
      pq <- Factorer.threadPool.use { ec =>
        Factorer
          .findFactorsCats(target)
          .evalOn(ec)
      }
    } yield assertEquals(pqTest, pq)
  }
  
  test(
    "fs2: 13533571 = 1709 x 7919"
  ) {
    val pqTest = (1709, 7919)
    val target = 13533571
    val targetDivLim = 10000
    for {
      pq <- Factorer.threadPool.use { ec =>
        Factorer
          .findFactorsFS2Stream(target)
          .evalOn(ec)
      }
    } yield assertEquals(pqTest, pq)
  }
  
  test(
    "fs2: 1993 = 1993 x 1"
  ) {
    val pqTest = (1, 1993)
    val target = 1993
    val targetDivLim = 2000
    for {
      pq <- Factorer.threadPool.use { ec =>
        Factorer
          .findFactorsFS2Stream(target)
          .evalOn(ec)
      }
    } yield assertEquals(pqTest, pq)
  }

}
