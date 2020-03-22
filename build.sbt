lazy val scalagb = (project in file("scalagb")).settings(
  organization := "tf.bug",
  name := "scalagb",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.1.1",
    "org.typelevel" %% "cats-tagless-macros" % "0.11",
    "org.typelevel" %% "spire" % "0.17.0-M1",
  ),
  scalacOptions ++= Seq(
    "-Ymacro-annotations",
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
)
