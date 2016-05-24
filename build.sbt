name := "play-slick-advanced"

version := "2.0.0"

scalaVersion := "2.11.8"

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies ++= Seq(
  "org.virtuslab" %% "unicorn-play" % "1.1.0",
  "com.h2database" % "h2" % "1.4.190",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)
libraryDependencies += evolutions

lazy val `play-slick-advanced` = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

routesGenerator := InjectedRoutesGenerator

fork in run := true