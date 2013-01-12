organization := "com.ansvia.belajar"

name := "TestingExample"

version := "0.0.1"

scalaVersion := "2.9.1"

resolvers ++= Seq(
    "Ansvia Repo" at "http://scala.repo.ansvia.com/releases"
    )

libraryDependencies ++= Seq(
    "com.ansvia" % "ansvia-commons" % "0.0.2",
    "org.specs2" % "specs2_2.9.1" % "1.12.3"
    )
