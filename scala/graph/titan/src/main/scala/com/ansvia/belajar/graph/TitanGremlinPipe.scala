
package com.ansvia.belajar.graph

import com.thinkaurelius.titan.core.TitanFactory
import com.tinkerpop.blueprints.{Edge, Graph, Vertex, Direction}
import org.apache.commons.configuration.BaseConfiguration
import com.ansvia.perf.PerfTiming
import java.lang.Iterable
import java.util
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.tinkerpop.pipes.PipeFunction
import com.tinkerpop.pipes.branch.LoopPipe.LoopBundle
import com.tinkerpop.gremlin.java.GremlinPipeline


object TitanGremlinPipe extends PerfTiming {

    import com.ansvia.graph.BlueprintsWrapper._

    val userData = Map(
        "gondez" -> "Solo",
        "temon" -> "Jogja",
        "robin" -> "Wonosobo",
        "andrie" -> "Belitung",
        "rizky" -> "Malang"
    )


    def main(args:Array[String]){

//        val uri = "/tmp/titandb"
        implicit val db = TitanFactory.openInMemoryGraph()

        val userMap = for ((name, location) <- userData) yield {
            val v = db.addVertex(null)
            v.setProperty("name", name)
            v.setProperty("location", location)
            (name, v)
        }

        val gondez = userMap("gondez")
        val andrie = userMap("andrie")
        val rizky = userMap("rizky")
        val temon = userMap("temon")
        val robin = userMap("robin")

        gondez --> "support" --> userMap("temon")
        userMap("temon") --> "support" --> userMap("robin")
        temon --> "knows" --> andrie
        temon --> "loves" --> rizky --> "knows" --> temon
        temon --> "support" --> gondez --> "knows" --> robin --> "knows" --> gondez
        andrie --> "knows" --> robin

        timing("who is supported by gondez (get from gremlin pipe)"){
            val pipe = new GremlinPipeline[Vertex, AnyRef]()
            pipe.start(db.getVertex(gondez.getId)).out("support").property("name")
            while(pipe.hasNext){
                val x = pipe.next()
                println(x)
            }
        }

        timing("who is supported by temon (get from gremlin pipe)"){
            val pipe = new GremlinPipeline[Vertex, AnyRef]()
            pipe.start(db.getVertex(userMap("temon").getId)).out("support").property("name")
            while(pipe.hasNext){
                val x = pipe.next()
                println(x)
            }
        }

        timing("who is supported of supported by gondez (get from gremlin pipe)"){
            val pipe = new GremlinPipeline[Vertex, AnyRef]()
            pipe.start(db.getVertex(userMap("gondez").getId)).out("support").out("support").property("name")
            while(pipe.hasNext){
                val x = pipe.next()
                println(x)
            }
        }

        timing("who is supported of supported by gondez (using loop)"){
            val pipe = new GremlinPipeline[Vertex, AnyRef]()
            pipe.start(db.getVertex(userMap("gondez").getId)).out("support").loop(1,new PipeFunction[LoopBundle[Vertex],java.lang.Boolean] {
                def compute(p1: LoopBundle[Vertex]) = {
                    false
                }
            }).property("name")
            while(pipe.hasNext){
                val x = pipe.next()
                println(x)
            }
        }

        timing("all temon's out"){
            temon.pipe.outE().toList.foreach { e =>
                e match {
                    case edge:Edge => println(edge.prettyPrint("name"))
                    case unknown => println("unknown type: " + unknown.getClass.getName)
                }
            }
        }

        timing("who is known by gondez?"){
            gondez.pipe.out("knows").toList.foreach( u => println(" + " + u.getProperty("name")) )
        }

        timing("robin's mutual knows"){
            val x = robin.pipe.both("knows").toList //.foreach( u => println(" + " + u.getProperty("name")) )
            val mutual = x.filter { v => x.count( vv => vv == v ) == 2 }.distinct.toList
            mutual.foreach( u => println(" + " + u.getProperty("name") + " from " + u.getProperty("location")) )
        }

        timing("who is knows robin?"){
            robin.pipe.in("knows").toList.foreach( u => println(" + " + u.getProperty("name")) )
        }

        timing("who is knows temon?"){
            temon.pipe.in("knows").toList.foreach( u => println(" + " + u.getProperty("name")) )
        }

        timing("all temon's out with skip"){
            temon.pipe.out().range(1, 2).toList.foreach( u => println(" + " + u.getProperty("name")) )
        }


        db.shutdown()


    }
}