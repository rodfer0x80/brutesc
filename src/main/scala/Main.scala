import cats.effect.{IO, IOApp, Resource}
import cats.implicits._
import scala.concurrent.ExecutionContext

object Factorer {
  
  // cpu bound parallel (threadNum = coreNum) 
  val threadNum: Int = 4
  val threadPool: Resource[IO, ExecutionContext] =
    Resource.make(
      IO(
        ExecutionContext.fromExecutorService(
          java.util.concurrent.Executors.newFixedThreadPool(threadNum)
        )
      )
    )(ec => IO(ec.shutdown()))

  def findFactors(target: Int, targetDivLim: Int): IO[(Int, Int)] = {
    val threadChunk: Int = targetDivLim / threadNum
    val chunks =
      (0 until threadNum).map(i => (i * threadChunk + 1, (i + 1) * threadChunk))
    val factors = chunks.toList.parTraverse { case (start, end) =>
      IO {
        (start to end)
          .collectFirst {
            case n if target % n == 0 => (n, target / n)
          }
          .getOrElse((0, 0))
      }
    }
    // reverse the list since the first result will always be (1, target)
    // and we only want that result for pure primes, but for bruteforcing
    // primes we generally are looking for (p, q) such that p != 1 and  q != 1
    factors.map { factor =>
      factor.reverse
        .collectFirst { case (p, q) if p != 0 && q != 0 => (p, q) }
        .getOrElse((0, 0))
    }
  }

}

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    val target: Int = 13533571
    // 0 < p and q <= targetDivLim where p and q are the divisor of a prime number
    val targetDivLim: Int = 10000

    for {
      _ <- IO { println(s"target: ${target}") }
      _ <- IO { println(s"targetDivLim: ${targetDivLim}") }
      (p, q) <- Factorer.threadPool.use { ec =>
        Factorer
          .findFactors(target, targetDivLim)
          .evalOn(ec)
      }
      _ <- IO { println(s"p: ${p}") }
      _ <- IO { println(s"q: ${q}") }
    } yield ()
  }
}
