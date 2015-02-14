name := """actorevents"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  // jdbc,
  // anorm,
  cache,
  ws
)

libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.9.19"
