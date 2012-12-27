
package com.ansvia.belajar.graph

import com.thinkaurelius.titan.core.TitanFactory
import com.tinkerpop.blueprints.Direction
import org.apache.commons.configuration.BaseConfiguration


object TitanExample {

    def main(args:Array[String]){

        // local
//        val g = TitanFactory.open("/tmp/titandb")

        // using cassandra
        val conf = new BaseConfiguration()
        conf.setProperty("storage.backend", "cassandra")
        conf.setProperty("storage.hostname", "127.0.0.1")
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

        var result = robin.query().labels("support").vertices()

        println("Who is supported by robin")

        var it = result.iterator()
        while(it.hasNext){
            val v = it.next()
            println(" + " + v.getProperty("name"))
        }

        println("Who is supported by robin with cool")

        supportE.setProperty("cool", true)

        result = robin.query().labels("support").has("cool", true).vertices()
        it = result.iterator()
        while (it.hasNext){
            val v = it.next()
            println(" + " + v.getProperty("name"))
        }

        println("who is supporting andrie")

        it = andrie.getVertices(Direction.IN, "support").iterator()
        while(it.hasNext){
            val v = it.next()
            println(" + " + v.getProperty("name"))
        }


    }
}