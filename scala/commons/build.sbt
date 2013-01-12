organization := "com.ansvia.belajar.commons"

name := "CommonsExample"

version := "0.0.1"

scalaVersion := "2.9.1"

resolvers ++= Seq(
    "Ansvia Repo" at "http://scala.repo.ansvia.com/releases"
    )

libraryDependencies ++= Seq(
    "com.ansvia" % "ansvia-commons" % "0.0.2",
    "ch.qos.logback" % "logback-classic" % "1.0.9"
    )
