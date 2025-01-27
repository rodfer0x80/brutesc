import cats.effect.{IO, IOApp, Resource}
import cats.implicits.*

import fs2.Stream

import scala.io.Source
import scala.util.Using
import scala.concurrent.ExecutionContext
import scala.math.sqrt

import java.util.concurrent.Executors
import java.nio.file.{Path, StandardOpenOption}
import java.io.{BufferedWriter, FileWriter}

object FileOps {
  def read(filePath: String): Either[String, List[String]] = {
    Using(Source.fromFile(filePath)) { source =>
      source.getLines().toList
    }.toEither.leftMap(_.getMessage)
  }

  def write(filePath: String, data: String): Either[String, Unit] = {
    Using(new BufferedWriter(new FileWriter(filePath, true))) { writer =>
      writer.write(data)
    }.toEither.leftMap(_.getMessage)
  }

}

object Factorer {
  val threadNum: Int = 4
  val tablePath: String = "./src/main/resources/rainbow_table.csv"

  val threadPool: Resource[IO, ExecutionContext] =
    Resource.make(
      IO(
        ExecutionContext.fromExecutorService(
          Executors.newFixedThreadPool(threadNum)
        )
      )
    )(ec => IO(ec.shutdown()))

  def readFromTable(target: Int): IO[(Int, Int)] = {
    IO {
      FileOps.read(tablePath) match {
        case Right(lines) =>
          lines
            .collectFirst {
              case line if line.startsWith(s"$target,") =>
                val cols = line.split(",")
                (cols(1).toInt, cols(2).toInt)
            }
            .getOrElse((1, 1))

        case Left(error) =>
          // println(error)
          (1, 1)
      }
    }
  }

  def writeToTable(target: Int, p: Int, q: Int): IO[Unit] = {
    IO {
      val line = s"$target,$p,$q\n"
      FileOps.write(tablePath, line) match {
        case Right(_) =>
          ()
        case Left(error) =>
          throw new RuntimeException(error)
      }
    }
  }

  def findFactorsCats(target: Int): IO[(Int, Int)] = {
    val maxFactor: Int = sqrt(target).toInt
    val threadChunk: Int = maxFactor / threadNum
    val chunks: List[(Int, Int)] = (0 until threadNum)
      .map(i => (i * threadChunk + 1, (i + 1) * threadChunk))
      .toList
    val factors = chunks.parTraverse { case (start, end) =>
      IO {
        (start to end)
          .collectFirst {
            case n if n != 1 && n != target && target % n == 0 =>
              (n, target / n)
          }
          .getOrElse((1, target))
      }
    }
    factors.map { factor =>
      factor
        .collectFirst { case (p, q) if p != 1 && q != target => (p, q) }
        .getOrElse((1, target))
    }
  }

  def findFactorsFS2Stream(target: Int): IO[(Int, Int)] = {
    val maxFactor: Int = sqrt(target).toInt
    val threadChunk: Int = maxFactor / threadNum
    val chunks: List[(Int, Int)] = (0 until threadNum)
      .map(i => (i * threadChunk + 1, (i + 1) * threadChunk))
      .toList
    Stream
      .emits(chunks)
      .covary[IO]
      .parEvalMap(threadNum) { case (start, end) =>
        IO {
          (start to end)
            .collectFirst {
              case n if n != 1 && n != target && target % n == 0 =>
                (n, target / n)
            }
            .getOrElse((1, target))
        }
      }
      .compile
      .toList
      .map {
        _.collectFirst { case (p, q) if p != 1 && q != target => (p, q) }
          .getOrElse((1, target))
      }
  }

  def find(target: Int): IO[(Int, Int)] = {
    readFromTable(target).flatMap { case (p, q) =>
      if ((p, q) != (1, 1)) {
        IO.pure((p, q))
      } else {
        threadPool
          .use { ec =>
            Factorer
              .findFactorsFS2Stream(target)
              .evalOn(ec)
          }
          .flatMap { (p, q) =>
            writeToTable(target, p, q) *>
              IO { (p, q) }
          }
      }
    }
  }

}

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    val target: Int = 13533571
    // 0 < p and q <= targetDivLim where p and q are the divisor of a prime number

    for {
      _ <- IO { println(s"target: ${target}") }
      (p, q) <- Factorer.find(target)
      _ <- IO { println(s"p: ${p}") }
      _ <- IO { println(s"q: ${q}") }
    } yield ()
  }
}
