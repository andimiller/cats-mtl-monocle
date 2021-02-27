package net.andimiller.cats.mtl.monocle

import cats.{Applicative, Monad}
import cats.mtl.{Ask, Stateful}
import monocle.Lens
import cats.implicits._

trait Instances {

  implicit def askWithLens[F[_]: Applicative, I, O](implicit a: Ask[F, I], lens: Lens[I, O]): Ask[F, O] =
    new Ask[F, O] {
      override def applicative: Applicative[F] = a.applicative
      override def ask[E2 >: O]: F[E2] = a.ask.map(lens.get)
    }

  implicit def statefulWithBoth[F[_]: Monad, I, O](implicit s: Stateful[F, I], lens: Lens[I, O]): Stateful[F, O] =
    new Stateful[F, O] {
      override def monad: Monad[F] = s.monad
      override def get: F[O] = s.get.map(lens.get)
      override def set(o: O): F[Unit] = s.modify(lens.modify(_ => o))
    }

}
