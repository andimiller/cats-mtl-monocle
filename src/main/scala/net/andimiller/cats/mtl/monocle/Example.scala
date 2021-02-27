package net.andimiller.cats.mtl.monocle

import cats.Monad
import cats.data.ReaderT
import cats.effect._
import cats.implicits._
import cats.mtl.implicits._
import cats.mtl.Ask
import monocle.Lens
import monocle.macros.GenLens

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object Example extends IOApp {

  // imagine we had some config class
  case class ClientConfig(timeout: FiniteDuration)
  // and it was nested inside a big config class
  case class Config(clientConfig: ClientConfig)
  object Config {
    // and we had a lens which could access it
    implicit val clientLens: Lens[Config, ClientConfig] = GenLens[Config](_.clientConfig)
  }

  // we might write some of our program needing the inner config class
  def program[F[_]: Monad: Sync](implicit a: Ask[F, ClientConfig]) =
    for {
      clientConfig <- a.ask
      _            <- Sync[F].delay { println(s"here I might do something with the config: $clientConfig") }
    } yield ExitCode.Success

  override def run(args: List[String]): IO[ExitCode] =
    // while actually passing in the outer config class, and allowing the Ask to be derived since the Lens is available
    program[ReaderT[IO, Config, *]].run(Config(ClientConfig(123.seconds)))
}
