name := """play-scala-starter-example"""
herokuAppName in Compile := "serene-temple-90721"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.4"


libraryDependencies ++= Seq(
  guice,
  "mysql" % "mysql-connector-java" % "6.0.5",
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-codegen" % "3.2.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.2",
  "com.koddi" %% "geocoder" % "1.1.0"
)

