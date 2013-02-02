import sbt._
import Keys._

// Author: Robin

object Build extends Build {
	import BuildSettings._
	import Dependencies._

  	// root
	lazy val root = Project("root", file("."))
		.aggregate(actorExample)
		.settings(basicSettings: _*)
		.settings(noPublishing: _*)
		
	// modules

	lazy val actorExample = Project("actor-example", file("actor-example"))
		.settings(moduleSettings: _*)
		.settings(libraryDependencies ++=
			compile(ansviaCommons, akkaActor, akkaRemote) ++
			test(specs2) ++
			runtime(logback)
		)
}

