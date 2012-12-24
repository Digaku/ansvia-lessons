organization := "com.ansvia.belajar.neo4j"

name := "Neo4jExample"

version := "0.0.1"

scalaVersion := "2.9.1"

resolvers ++= Seq(
	"Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
	"Ansvia repo" at "http://scala.repo.ansvia.com/releases/",
	"Fakod repo releases" at "https://raw.github.com/FaKod/fakod-mvn-repo/master/releases",
	"Fakod repo snapshot" at "https://raw.github.com/FaKod/fakod-mvn-repo/master/snapshots",
	"anormcypher" at "http://repo.anormcypher.org/"
)


libraryDependencies ++= Seq(
//    "org.neo4j" % "neo4j-kernel" % "1.7.2",
    "org.neo4j" % "neo4j-scala" % "0.1.1",
    "ch.qos.logback" % "logback-classic" % "1.0.7",
	"ch.qos.logback" % "logback-core" % "1.0.7",
	"org.slf4j" % "slf4j-api" % "1.6.6",
	"com.ansvia" % "ansvia-commons" % "0.0.2",
	"org.anormcypher" %% "anormcypher" % "0.2.1"
    )

