resolvers ++= Seq(
    "Ansvia repo" at "http://scala.repo.ansvia.com/releases"
    )

//addSbtPlugin("com.ansvia" % "onedir" % "0.4")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.1.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.0")
