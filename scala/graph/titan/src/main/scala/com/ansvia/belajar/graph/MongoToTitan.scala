
package com.ansvia.belajar.graph

import com.thinkaurelius.titan.core.TitanFactory
import com.tinkerpop.blueprints.{Vertex, Direction}
import org.apache.commons.configuration.BaseConfiguration
import com.ansvia.perf.PerfTiming
import java.lang.Iterable
import java.util
import com.mongodb.casbah.MongoConnection


object MongoToTitan extends PerfTiming {

    lazy val con = MongoConnection()
    lazy val db = con("digaku")
    lazy val userCol = db("user")

    def main(args:Array[String]){

        // local
//        val g = TitanFactory.open("/tmp/titandb")

        // using cassandra
        val conf = new BaseConfiguration()
        conf.setProperty("storage.backend", "cassandra")
        conf.setProperty("storage.hostname", "localhost")
        val g = TitanFactory.open(conf)

        g.createKeyIndex("name", classOf[Vertex])

    }
}