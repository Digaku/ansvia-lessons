
package com.ansvia.belajar.graph

import com.thinkaurelius.titan.core.TitanFactory


object TitanExample {

    def main(args:Array[String]){

        val g = TitanFactory.open("/tmp/titandb")

        val robin = g.addVertex(null)
        robin.setProperty("name", "robin")

        val andrie = g.addVertex(null)
        andrie.setProperty("name", "andrie")

        val supportE = g.addEdge(null, robin, andrie, "support")

        val result = robin.query().labels("support").vertices()

        println("Who is supported by robin")

        val it = result.iterator()
        while(it.hasNext){
            val v = it.next()
            println(" + " + v.getProperty("name"))
        }

    }
}