package com.ansvia.belajar.graph

import javax.persistence.{Version, Id}
import models.{Address, User}
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.ansvia.perf.PerfTiming
import com.tinkerpop.gremlin.java.GremlinPipeline
import com.tinkerpop.blueprints.{Direction, Graph, Vertex}
import com.tinkerpop.blueprints.impls.orient.{OrientEdge, OrientGraph}
import com.tinkerpop.pipes.PipeFunction
import com.tinkerpop.pipes.branch.LoopPipe.LoopBundle

//import com.orientechnologies.orient.core.db.graph.OGraphDatabase
//import com.orientechnologies.orient.core.record.impl.ODocument


object OrientdbGraphExample extends PerfTiming {

//    import Implicits._

    class VertexWrapper(vertex:Vertex, label:String, db:Graph){
        def --(label:String):VertexWrapper = {
            new VertexWrapper(vertex, label, db)
        }
        def -->(outV:Vertex){
            assert(label != null, "no label?")
            db.addEdge(null, vertex, outV, label)
        }
        def pipe = {
            val pipe = new GremlinPipeline[Vertex, AnyRef]()
            pipe.start(vertex)
        }
    }
    implicit def vertexWrapper(vertex:Vertex)(implicit db:OrientGraph) = {
        new VertexWrapper(vertex, null, db)
    }
    implicit def orientEdgeFormatter(edge:OrientEdge) = new {
        def prettyPrint(key:String) = {
            val in = edge.getVertex(Direction.IN)
            val label = edge.getLabel
            val out = edge.getVertex(Direction.OUT)
            "%s --%s--> %s".format(out.getProperty(key), label, in.getProperty(key))
        }
    }

    val userData = Map(
    "gondez" -> "Solo",
    "temon" -> "Jogja",
    "robin" -> "Wonosobo",
    "andrie" -> "Belitung",
    "rizky" -> "Malang"
    )


    def main(args:Array[String]){

        val uri = "memory:OrientdbGraphExample"
        implicit val db = new OrientGraph(uri, "admin", "admin")

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

        gondez --"support"--> userMap("temon")
        userMap("temon") --"support"--> userMap("robin")
        temon --"knows"--> andrie
        temon --"hate"--> rizky
        temon --"support"--> gondez

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
                    p1.getLoops < 3
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
                    case edge:OrientEdge => println(edge.prettyPrint("name"))
                    case _ =>
                }
            }
        }


        db.shutdown()

//
////        if (db.exists())
////            db.open("admin", "admin")
////        else{
//            db.create()
////        }
//
//
//        db.createVertexType("User")
//
//        val gondez = db.createVertex("User").field("name", "gondez").save()
//        val temon = db.createVertex("User").field("name", "temon").save()
//        val supportE = db.createEdgeType("Support")
//
//        db.createEdge(gondez, temon, "Support")
//
//        // dapetin semua vertex
//        var rv = db.queryBySql[ODocument]("select from User")
//
//        for (doc <- rv){
//            println(" + " + doc.field("name"))
//        }
//
//        timing("get temon's supporters"){
//            rv = db.queryBySql[ODocument]("select GREMLIN(current.outE.filter{it['@class']=='Support'}.inV).name from User where name='temon'")
//            db.command(new OCommandGremlin)
//            rv.foreach( u => println(" + " + u))
//        }
//
//
//
//        // dapetin vertex by edge
//
//
//        db.drop()
//        db.close()

        //      val oclass = db.getMetadata.getSchema.createClass(classOf[User])
        //
        //      val user1 = db.save(new User())


    }
}


