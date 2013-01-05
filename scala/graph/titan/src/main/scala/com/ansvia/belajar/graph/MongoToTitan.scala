
package com.ansvia.belajar.graph

import com.thinkaurelius.titan.core.TitanFactory
import com.tinkerpop.blueprints.{Vertex, Direction}
import org.apache.commons.configuration.BaseConfiguration
import com.ansvia.perf.PerfTiming
import java.lang.Iterable
import java.util
import com.mongodb.casbah.Imports._
import com.ansvia.commons.logging.Slf4jLogger
import com.tinkerpop.blueprints.TransactionalGraph.Conclusion

object MongoToTitan extends PerfTiming with Slf4jLogger {

    lazy val con = MongoConnection()
    lazy val db = con("digaku")
    lazy val userCol = db("user")
    lazy val postCol = db("user_post")
    lazy val chCol = db("channel")
    lazy val respCol = db("response")


    def main(args:Array[String]){

        // local
//        val g = TitanFactory.open("/tmp/titandb")

        // using cassandra
        val conf = new BaseConfiguration()
        conf.setProperty("storage.backend", "cassandra")
        conf.setProperty("storage.hostname", "localhost")
        val g = TitanFactory.open(conf)


        var idx = 0
        var offset = 0
        var done = false
        while(!done){
            val rv = userCol.find(MongoDBObject()).skip(offset).limit(100)
            if (rv.count > 0){

                for (u <- rv){

                    info("Processing %s...".format(u.getAs[String]("name").get))

                    g.startTransaction()

                    val oid = u.get("_id").toString
                    val uv = g.addVertex(null)

                    uv.setProperty("oid", u.getAsOrElse[String]("oid", oid))
                    uv.setProperty("name", u.getAsOrElse[String]("name", ""))
                    uv.setProperty("lower_name", u.getAsOrElse[String]("lower_name", ""))
                    uv.setProperty("full_name", u.getAsOrElse[String]("full_name", ""))
                    uv.setProperty("full_name", u.getAsOrElse[String]("full_name", ""))
                    uv.setProperty("email", u.getAsOrElse[String]("email_login", ""))
                    uv.setProperty("sdescs", u.getAsOrElse[scala.List[String]]("personal_descs", scala.List.empty[String]))
                    uv.setProperty("location", u.getAsOrElse[String]("location", ""))
                    uv.setProperty("level", u.getAsOrElse[Int]("_level", 0))
                    uv.setProperty("point", u.getAsOrElse[Int]("_point", 0))
                    uv.setProperty("photo_small", u.getAsOrElse[String]("photo_small", ""))
                    uv.setProperty("photo_medium", u.getAsOrElse[String]("photo_medium", ""))
                    uv.setProperty("photo_large", u.getAsOrElse[String]("photo_large", ""))

                    g.stopTransaction(Conclusion.SUCCESS)


//                    val fuv = g.getVertices("oid", oid)
//                    debug("verifiying %s... ok".format(fuv))

                    idx += 1
                }

                offset = offset + 100
            }else{
                done = true
            }
        }

//
//
//
//        val robin = g.addVertex(null)
//        robin.setProperty("name", "robin")
//
//        val andrie = g.addVertex(null)
//        andrie.setProperty("name", "andrie")
//
//        val temon = g.addVertex(null)
//        temon.setProperty("name", "temon")
//
//        val adit = g.addVertex(null)
//        adit.setProperty("name", "Adit")
//
//
//        val supportE = g.addEdge(null, robin, andrie, "support")
//        val supportE2 = g.addEdge(null, robin, temon, "support")
//        val supportE3 = g.addEdge(null, adit, andrie, "support")
//
//        var result: Iterable[Vertex] = null
//        var it: util.Iterator[Vertex] = null
//
//        timing("Who is supported by robin"){
//            result = robin.query().labels("support").vertices()
//            it = result.iterator()
//            while(it.hasNext){
//                val v = it.next()
//                println(" + " + v.getProperty("name"))
//            }
//        }
//
//
//        timing("Who is supported by robin with cool"){
//            supportE.setProperty("cool", true)
//
//            result = robin.query().labels("support").has("cool", true).vertices()
//            it = result.iterator()
//            while (it.hasNext){
//                val v = it.next()
//                println(" + " + v.getProperty("name"))
//            }
//        }
//
//
//        timing("who is supporting andrie"){
//            it = andrie.getVertices(Direction.IN, "support").iterator()
//            while(it.hasNext){
//                val v = it.next()
//                println(" + " + v.getProperty("name"))
//            }
//        }
//
//        g.removeVertex(andrie)
//        g.removeVertex(temon)
//        g.removeVertex(adit)
//        g.removeVertex(robin)

//        g.removeEdge(supportE)
//        g.removeEdge(supportE2)
//        g.removeEdge(supportE3)


    }
}