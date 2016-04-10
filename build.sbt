name := """rkablog-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test,
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "org.webjars" % "bootstrap" % "3.3.6"
)

libraryDependencies += filters
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.1.1"
libraryDependencies += "de.svenkubiak" % "jBCrypt" % "0.4.1"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
