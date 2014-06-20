name := "play-slick-advanced"

version := "2.0.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  // Select Play modules
  //jdbc,      // The JDBC connection pool and the play.api.db API
  //anorm,     // Scala RDBMS Library
  //javaJdbc,  // Java database API
  //javaEbean, // Java Ebean plugin
  //javaJpa,   // Java JPA plugin
  //filters,   // A set of built-in filters
  javaCore,  // The core Java API
  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.2.1",
  "org.webjars" % "bootstrap" % "2.3.2",
  "org.virtuslab" %% "unicorn-play" % "0.5.2"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

play.Project.playScalaSettings
