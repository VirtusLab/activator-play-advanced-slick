name := "play-slick-advanced"

version := "2.0.0"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.6", scalaVersion.value)

libraryDependencies ++= Seq(
  "org.virtuslab" %% "unicorn-play" % "1.0.0",
  "com.h2database" % "h2" % "1.4.190" % "test",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

lazy val `play-slick-advanced` = (project in file(".")).enablePlugins(PlayScala, SbtWeb)


fork in run := true