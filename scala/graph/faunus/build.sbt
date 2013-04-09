organization := "com.ansvia.belajar.graph"

name := "belajar-faunus"

version := "0.0.1"

scalaVersion := "2.9.1"

resolvers ++= Seq(
    "Ansvia repo" at "http://scala.repo.ansvia.com/releases",
    "Local m2 repo" at "file:///" + Path.userHome + "/.m2/repository",
    "Oracle repo" at "http://download.oracle.com/maven",
    "Maven central" at "http://repo1.maven.org/maven2",
    "Sonatype releases repo" at "https://oss.sonatype.org/content/repositories/releases"
    )
    
libraryDependencies ++= Seq(
    "com.thinkaurelius.titan" % "titan" % "0.2.0",
    "com.google.guava" % "guava" % "11.0.2" % "compile",
    "com.ansvia" % "ansvia-perf" % "0.0.2",
    "com.ansvia" % "ansvia-commons" % "0.0.6",
    "org.mongodb" % "casbah_2.9.1" % "2.4.1",
    "com.ansvia.graph" %% "blueprints-scala" % "0.0.7",
    "com.thinkaurelius.faunus" % "faunus" % "0.1.0" excludeAll(ExclusionRule("org.openrdf.sesame"))
    )
