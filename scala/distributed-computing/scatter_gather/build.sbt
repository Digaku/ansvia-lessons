organization := "com.ansvia.belajar.scattergather"

name := "scatter-gather"

version := "0.0.1"

scalaVersion := "2.9.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-P:continuations:enable")

resolvers ++= Seq(
	"Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
	"Ansvia repo" at "http://scala.repo.ansvia.com/releases/"
)

ivyXML :=
  <dependencies>
    <!--<exclude module="log4j" />-->
    <exclude module="slf4j-log4j12" />
  </dependencies>



libraryDependencies ++= Seq(
	"ch.qos.logback" % "logback-classic" % "1.0.7",
	"ch.qos.logback" % "logback-core" % "1.0.7",
	"org.slf4j" % "slf4j-api" % "1.6.6",
	"com.typesafe.akka" % "akka-actor" % "2.0.3",
	"com.typesafe.akka" % "akka-remote" % "2.0.3",
	"org.jboss.netty" % "netty" % "3.2.7.Final",
	"com.ansvia" % "ansvia-commons" % "0.0.2"
)


autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.1") }


