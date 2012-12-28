
package com.ansvia.belajar.graph

import com.thinkaurelius.titan.core.TitanFactory
import com.tinkerpop.blueprints.{Vertex, Direction}
import org.apache.commons.configuration.BaseConfiguration
import com.ansvia.perf.PerfTiming
import java.lang.Iterable
import java.util


object TitanExample extends PerfTiming {

    def main(args:Array[String]){

        // local
//        val g = TitanFactory.open("/tmp/titandb")

        // using cassandra
        val conf = new BaseConfiguration()
        conf.setProperty("storage.backend", "cassandra")
        conf.setProperty("storage.hostname", "localhost")
        val g = TitanFactory.open(conf)

        val robin = g.addVertex(null)
        robin.setProperty("name", "robin")

        val andrie = g.addVertex(null)
        andrie.setProperty("name", "andrie")

        val temon = g.addVertex(null)
        temon.setProperty("name", "temon")

        val adit = g.addVertex(null)
        adit.setProperty("name", "Adit")


        val supportE = g.addEdge(null, robin, andrie, "support")
        val supportE2 = g.addEdge(null, robin, temon, "support")
        g.addEdge(null, adit, andrie, "support")

        var result: Iterable[Vertex] = null
        var it: util.Iterator[Vertex] = null

        timing("Who is supported by robin"){
            result = robin.query().labels("support").vertices()
            it = result.iterator()
            while(it.hasNext){
                val v = it.next()
                println(" + " + v.getProperty("name"))
            }
        }


        timing("Who is supported by robin with cool"){
            supportE.setProperty("cool", true)

            result = robin.query().labels("support").has("cool", true).vertices()
            it = result.iterator()
            while (it.hasNext){
                val v = it.next()
                println(" + " + v.getProperty("name"))
            }
        }


        timing("who is supporting andrie"){
            it = andrie.getVertices(Direction.IN, "support").iterator()
            while(it.hasNext){
                val v = it.next()
                println(" + " + v.getProperty("name"))
            }
        }

    }
}