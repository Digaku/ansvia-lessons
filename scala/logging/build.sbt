organization := "com.ansvia.belajar.logging"

name := "LoggingExample"

version := "0.0.1"

scalaVersion := "2.9.1"

resolvers ++= Seq(
    "Ansvia Repo" at "http://scala.repo.ansvia.com/releases"
    )

libraryDependencies ++= Seq(
    "com.ansvia" % "ansvia-commons" % "0.0.2"
    )
