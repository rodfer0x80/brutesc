val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "brutesc",
    version := "1.33.7",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % "3.11.0",
      "co.fs2" %% "fs2-io" % "3.11.0",
      "co.fs2" %% "fs2-reactive-streams" % "3.11.0",
      "org.typelevel" %% "cats-core" % "2.12.0",
      "org.typelevel" %% "cats-effect" % "3.5.7"
    ),
    libraryDependencies += "org.typelevel" %% "munit-cats-effect" % "2.0.0" % "test",
    scalacOptions ++= Seq("-explain"),
    Compile / run / fork := true
  )
