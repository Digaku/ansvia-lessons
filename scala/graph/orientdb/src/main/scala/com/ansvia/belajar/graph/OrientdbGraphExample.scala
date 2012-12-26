package com.ansvia.belajar.graph

import javax.persistence.{Version, Id}
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.db.`object`.ODatabaseObject
import models.{Address, User}
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.db.graph.{OGraphDatabase, OGraphDatabasePooled}
import com.orientechnologies.orient.core.Orient
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
import com.orientechnologies.orient.core.db.ODatabase
import com.orientechnologies.orient.core.sql.query
import com.orientechnologies.orient.core.record.impl.ODocument


object OrientdbGraphExample {


    implicit def dbWrapper(db: ODatabaseObject) = new {
        def queryBySql[T](sql: String, params: AnyRef*): List[T] = {
            val params4java = params.toArray
            val results: java.util.List[T] = db.query(new OSQLSynchQuery[T](sql), params4java: _*)
            results.asScala.toList
        }
    }

    def main(args:Array[String]){

        val uri = "local:/tmp/orientdb"
        val orient = Orient.instance()
        val db = orient.getDatabaseFactory.createGraphDatabase(uri)

        if (db.exists())
            db.open("admin", "admin")
        else{
            db.create()
        }

        db.createVertexType("User")

        val robin = db.createVertex("User").field("name", "robin").save()
        val anis = db.createVertex("User").field("name", "anis").save()

        var edge = db.createEdge(robin, anis)
        edge.field("loves")
        edge.save()

        // dapetin semua vertex
        val rv = db.query(new query.OSQLSynchQuery[ODocument]("select from User")).asInstanceOf[java.util.List[ODocument]].asScala.toList

        for (doc <- rv){
            println(" + " + doc.field("name"))
        }

        // dapetin vertex by edge


        db.drop()
        db.close()

        //      val oclass = db.getMetadata.getSchema.createClass(classOf[User])
        //
        //      val user1 = db.save(new User())


    }
}


