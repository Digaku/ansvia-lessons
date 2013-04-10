
package com.ansvia.belajar.graph

import org.apache.commons.configuration.BaseConfiguration
import com.ansvia.perf.PerfTiming
import com.thinkaurelius.faunus.{FaunusGraph, FaunusPipeline}
import com.thinkaurelius.faunus.formats.titan.cassandra.FaunusTitanCassandraGraph
import scala.collection.JavaConversions._
import org.apache.hadoop.conf.Configuration
import java.io.{BufferedReader, FileReader, File}
import com.thinkaurelius.faunus.formats.graphson.GraphSONOutputFormat


object Faunus extends PerfTiming {


    val userData = Map(
        "gondez" -> "Solo",
        "temon" -> "Jogja",
        "robin" -> "Wonosobo",
        "andrie" -> "Belitung",
        "rizky" -> "Malang"
    )


    def main(args:Array[String]){


        //g.E.transform('{"{\\"_inV\\":\\"" + it.inV.toList()[0].id + "\\", \\"label\\":\\"" + it.label + "\\", \\"_outV\\":\\"" + it.outV.toList()[0].id + "\\",\\"attr\\":\\"" + it.map() + "\\"}"}')

        val config = new Configuration()
//        faunus.graph.input.format=com.thinkaurelius.faunus.formats.titan.cassandra.TitanCassandraInputFormat
//        faunus.graph.input.titan.storage.backend=cassandra
//        faunus.graph.input.titan.storage.hostname=localhost
//        faunus.graph.input.titan.storage.port=9160
//        faunus.graph.input.titan.storage.keyspace=titan
//        cassandra.input.partitioner.class=org.apache.cassandra.dht.RandomPartitioner
//        # cassandra.input.split.size=512
//
//        # output data (graph or statistic) parameters
//                faunus.graph.output.format=com.thinkaurelius.faunus.formats.graphson.GraphSONOutputFormat
//        faunus.sideeffect.output.format=org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
//        faunus.output.location=output
//        faunus.output.location.overwrite=true


        config.set("faunus.graph.input.format", "com.thinkaurelius.faunus.formats.titan.cassandra.TitanCassandraInputFormat")
        config.set("faunus.graph.input.titan.storage.backend", "cassandra")
        config.set("faunus.graph.input.titan.storage.hostname", "localhost")
        config.set("faunus.graph.input.titan.storage.port", "9160")
        config.set("faunus.graph.input.titan.storage.keyspace", "titan")
        config.set("cassandra.input.partitioner.class", "org.apache.cassandra.dht.RandomPartitioner")
        config.set("faunus.graph.output.format", "com.thinkaurelius.faunus.formats.graphson.GraphSONOutputFormat")
        config.set("faunus.sideeffect.output.format", "org.apache.hadoop.mapreduce.lib.output.TextOutputFormat")
        config.set("faunus.output.location", "/tmp/faunus-output")
        config.set("faunus.output.location.overwrite", "true")

//        config.setProperty("faunus.graph.input.titan.storage.backend", "cassandra")
//        config.setProperty("faunus.graph.input.titan.storage.hostname", "localhost")
//        config.setProperty("faunus.graph.input.titan.storage.port", "9160")
//        config.setProperty("faunus.graph.input.titan.storage.keyspace", "titan")
//        config.setProperty("faunus.graph.output.format", "com.thinkaurelius.faunus.formats.noop.NoOpOutputFormat")
//        config.setProperty("cassandra.input.partitioner.class", "org.apache.cassandra.dht.RandomPartitioner")
//        config.setProperty("faunus.output.location", "/tmp/faunus-output")
//        config.set("hbase.mapreduce.scan.cachedrows", "1000")
//        config.set("cassandra.input.split.size", "512")
        val faun = new FaunusGraph(config)
//        val faun = new FaunusTitanCassandraGraph(config)
        val faunPipe = new FaunusPipeline(faun)

//        case "response_of":
//                attrs.push('"count":' + it.inV.inE("response_of").count())
//        break
//        case "view":
//                attrs.push('"count":' + it.inV.inE("view").count())
//        break
//        case "likes":
//                attrs.push('"count":' + it.inV.inE("likes").count())
//        break
//        case "wrote":
//                attrs.push('"time":' + "0")
//        break
//        case "origin":
//                attrs.push('"time":' + "0")
//        break

        faunPipe.E().transform(
            """{ it ->
              |  attrs = []
              |  it.map().each{ k, v ->
              |     def vStr = '"' + v + '"'
              |     switch (k){
              |         case "invitedByUserId":
              |             vStr = v
              |     }
              |     attrs.push('"' + k + '":' + vStr)
              |  }
              |  switch (it.label){
              |     case "support":
              |         attrs.push('"sourceId":' + it.outV.toList()[0].id)
              |         attrs.push('"targetId":' + it.inV.toList()[0].id)
              |         break
              |     case "join":
              |         attrs.push('"sourceId":' + it.outV.toList()[0].id)
              |         attrs.push('"targetId":' + it.inV.toList()[0].id)
              |         break
              |  }
              |  attrsStr = attrs.join(",")
              |  if (attrsStr != ""){
              |     attrsStr = "," + attrsStr
              |  }
              |  '{"_outV":' + it.outV.toList()[0].id + ',' +
              |    '"label":"' + it.label + '",' +
              |    '"_inV":' + it.inV.toList()[0].id +
              |    attrsStr + '}'
              |}""".stripMargin).submit()

        val _r = new File("/tmp/faunus-output/job-0/sideeffect-r-00000")
        val _m = new File("/tmp/faunus-output/job-0/sideeffect-m-00000")

        val sideF = if (_r.exists)
            _r
        else if(_m.exists())
            _m
        else
            _m

        val bfr = new BufferedReader(new FileReader(sideF))
        var done = false
        while(!done){
            val rv = bfr.readLine()
            if (rv!=null)
                println("==> " + bfr.readLine())
            else
                done = true
        }
        bfr.close()

//        faun.getEdges.foreach(println)

//
////        val uri = "/tmp/titandb"
//        implicit val db = TitanFactory.openInMemoryGraph()
//
//        val userMap = for ((name, location) <- userData) yield {
//            val v = db.addVertex(null)
//            v.setProperty("name", name)
//            v.setProperty("location", location)
//            (name, v)
//        }
//
//        val gondez = userMap("gondez")
//        val andrie = userMap("andrie")
//        val rizky = userMap("rizky")
//        val temon = userMap("temon")
//        val robin = userMap("robin")
//
//        gondez --> "support" --> userMap("temon")
//        userMap("temon") --> "support" --> userMap("robin")
//        temon --> "knows" --> andrie
//        temon --> "loves" --> rizky --> "knows" --> temon
//        temon --> "support" --> gondez --> "knows" --> robin --> "knows" --> gondez
//        andrie --> "knows" --> robin
//
//        timing("who is supported by gondez (get from gremlin pipe)"){
//            val pipe = new GremlinPipeline[Vertex, AnyRef]()
//            pipe.start(db.getVertex(gondez.getId)).out("support").property("name")
//            while(pipe.hasNext){
//                val x = pipe.next()
//                println(x)
//            }
//        }
//
//        timing("who is supported by temon (get from gremlin pipe)"){
//            val pipe = new GremlinPipeline[Vertex, AnyRef]()
//            pipe.start(db.getVertex(userMap("temon").getId)).out("support").property("name")
//            while(pipe.hasNext){
//                val x = pipe.next()
//                println(x)
//            }
//        }
//
//        timing("who is supported of supported by gondez (get from gremlin pipe)"){
//            val pipe = new GremlinPipeline[Vertex, AnyRef]()
//            pipe.start(db.getVertex(userMap("gondez").getId)).out("support").out("support").property("name")
//            while(pipe.hasNext){
//                val x = pipe.next()
//                println(x)
//            }
//        }
//
//        timing("who is supported of supported by gondez (using loop)"){
//            val pipe = new GremlinPipeline[Vertex, AnyRef]()
//            pipe.start(db.getVertex(userMap("gondez").getId)).out("support").loop(1,new PipeFunction[LoopBundle[Vertex],java.lang.Boolean] {
//                def compute(p1: LoopBundle[Vertex]) = {
//                    false
//                }
//            }).property("name")
//            while(pipe.hasNext){
//                val x = pipe.next()
//                println(x)
//            }
//        }
//
//        timing("all temon's out"){
//            temon.pipe.outE().toList.foreach { e =>
//                e match {
//                    case edge:Edge => println(edge.prettyPrint("name"))
//                    case unknown => println("unknown type: " + unknown.getClass.getName)
//                }
//            }
//        }
//
//        timing("who is known by gondez?"){
//            gondez.pipe.out("knows").toList.foreach( u => println(" + " + u.getProperty("name")) )
//        }
//
//        timing("robin's mutual knows"){
//            val x = robin.pipe.both("knows").toList //.foreach( u => println(" + " + u.getProperty("name")) )
//            val mutual = x.filter { v => x.count( vv => vv == v ) == 2 }.distinct.toList
//            mutual.foreach( u => println(" + " + u.getProperty("name") + " from " + u.getProperty("location")) )
//        }
//
//        timing("who is knows robin?"){
//            robin.pipe.in("knows").toList.foreach( u => println(" + " + u.getProperty("name")) )
//        }
//
//        timing("who is knows temon?"){
//            temon.pipe.in("knows").toList.foreach( u => println(" + " + u.getProperty("name")) )
//        }
//
//        timing("all temon's out with skip"){
//            temon.pipe.out().range(1, 2).toList.foreach( u => println(" + " + u.getProperty("name")) )
//        }
//
//
//        db.shutdown()


    }
}