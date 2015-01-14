name := "play-slick-advanced"

version := "2.0.0"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "org.virtuslab" %% "unicorn-play" % "0.6.1",
  "com.h2database" % "h2" % "1.4.181" % "test"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

lazy val `play-slick-advanced` = (project in file(".")).enablePlugins(PlayScala, SbtWeb)
