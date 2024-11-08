val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "brutesc",
    version := "1.33.7",
    scalaVersion := scala3Version,
    libraryDependencies += "org.typelevel" %% "munit-cats-effect" % "2.0.0" % "test",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.12.0",
      "org.typelevel" %% "cats-effect" % "3.5.4"
    ),
    scalacOptions ++= Seq("-explain"),
    Compile / run / fork := true
  )
