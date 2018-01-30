name := """play-scala-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  guice,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "mysql" % "mysql-connector-java" % "6.0.5",
  "com.typesafe.play" %% "play-slick" % "3.0.1",
  "com.typesafe.slick" %% "slick-codegen" % "3.2.1",
  "com.koddi" %% "geocoder" % "1.1.0"
)

