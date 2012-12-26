organization := "com.ansvia.belajar.graph"

name := "belajar-orientdb"

version := "0.0.1"

scalaVersion := "2.9.1"

resolvers ++= Seq(
    "Ansvia repo" at "http://scala.repo.ansvia.com/releases"
    )
    
libraryDependencies ++= Seq(
    "com.orientechnologies" % "orientdb-client" % "1.3.0",
    "org.eclipse.persistence" % "javax.persistence" % "2.0.0"
    )
