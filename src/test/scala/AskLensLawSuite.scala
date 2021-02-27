import cats._
import cats.data.ReaderT
import munit.DisciplineSuite
import cats.mtl.laws.discipline.AskTests
import cats.mtl.implicits._
import net.andimiller.cats.mtl.monocle._
import monocle.Lens
import cats.implicits._
import org.scalacheck.cats.implicits._
import org.scalacheck.Prop.{forAll => ∀}
import org.scalacheck.{Arbitrary, Cogen, Gen}
import org.typelevel.discipline.Laws
import cats.kernel.laws.discipline.catsLawsIsEqToProp
import cats.laws.discipline.DeprecatedEqInstances.catsLawsEqForFn1 // this is more feasible than the undeprecated one
import cats.mtl.Ask
import cats.laws.discipline.arbitrary._

case class ExampleCaseClass(a: Int, b: String)
object ExampleCaseClass {
  implicit val lens: Lens[ExampleCaseClass, Int] = Lens[ExampleCaseClass, Int](_.a)(a => e => e.copy(a = a))
  implicit val arb: Arbitrary[ExampleCaseClass] =
    Arbitrary(
      (Arbitrary.arbitrary[Int], Arbitrary.arbitrary[String]).mapN(ExampleCaseClass(_, _))
    )
  implicit val c: Cogen[ExampleCaseClass] =
    (Cogen[Int], Cogen[String]).contramapN { e => (e.a, e.b) }
}

class AskLensLawSuite extends DisciplineSuite with Laws {
  implicit def kleisliEq[F[_], A, B](implicit ev: Eq[A => F[B]]): Eq[ReaderT[F, A, B]] =
    Eq.by[ReaderT[F, A, B], A => F[B]](_.run)
  checkAll("AskLaws", AskTests[ReaderT[Id, ExampleCaseClass, *], Int].ask[String])
}
