name := "cats-mtl-monocle"

organization := "net.andimiller"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= List(
  "org.typelevel" %% "cats-mtl" % "1.1.2",
  "com.github.julien-truffaut" %% "monocle-core" % "2.1.0",
  "com.github.julien-truffaut" %% "monocle-macro" % "2.1.0",
  "org.typelevel" %% "cats-effect" % "2.3.3",
  "org.typelevel" %% "munit-cats-effect-2" % "0.13.1" % Test,
  "org.typelevel" %% "cats-mtl-laws" % "1.1.2" % Test,
  "org.typelevel" %% "discipline-munit" % "1.0.6" % Test,
  "io.chrisdavenport" %% "cats-scalacheck" % "0.3.0" % Test,
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full)

testFrameworks += new TestFramework("munit.Framework")