package com.ansvia.belajar.graph

import com.orientechnologies.orient.core.id.ORecordId
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import models.{DigakuModel, User}
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.db.`object`.ODatabaseObject
import com.orientechnologies.orient.`object`.db.OObjectDatabaseTx
import com.orientechnologies.orient.core.record.impl.ODocument

object OrientdbObjectExample {

    import com.ansvia.perf.Perf.timing
    import Implicits._


    def main(args:Array[String]){

        val uri = "memory:OrientdbObjectExample"
        //        val uri = "memory:OrientdbObjectExample"
        //        val orient = Orient.instance()
        val dbx = new OObjectDatabaseTx(uri)

        implicit val db:OObjectDatabaseTx =
            if (dbx.exists())
                dbx.open("admin", "admin")
            else{
                dbx.create()
            }

        //        db.getMetadata.getSchema.createClass(classOf[User])
//        db.getEntityManager.registerEntityClass(classOf[ODocument])
        db.getEntityManager.registerEntityClass(classOf[DigakuModel])
        db.getEntityManager.registerEntityClass(classOf[User])
        db.getEntityManager.registerEntityClasses("com.orientechnologies.orient.core.metadata.schema")

        //        db.getMetadata.reload()

        val gondez = new User("gondez")
        val robin = new User("robin")
        val temon = new User("temon")

        robin.supporting += gondez
        robin.supporting += temon
        temon.supporting += gondez

        db.save(robin)

        println("count class elements: " + db.countClusterElements("User"))


        var result: List[User] = db.queryBySql[User]("select * from User")

        timing("get from sql like query"){
            for ( u <- result ){
                //            db.detach(u)
                println(" + " + u)
            }
        }

        timing("Get from class browser"){

            val browser = db.browseClass(classOf[User])

            val it = browser.iterator()
            while (it.hasNext){
                val u = it.next()
                db.detach(u)
                println(" + " + u)
            }

        }

        //        println("search for gondez and support: ")

        timing("Search for gondez and support"){
            result=db.queryBySql[User]("select * from User where supporting contains (name='gondez')")

            for (u <- result){
                println(" + " + u)
            }
        }

        timing("Who is user supported by robin?"){
            val result = db.queryBySql[User]("select * from User where name='robin'")
            for(u <- result) {
                println(" + %s supporting %d users".format(u.name, u.supporting.size()))
                u.supporting.foreach(u => println("    * " + u.toString))
            }
        }


        db.drop()
        db.close()

        //      val oclass = db.getMetadata.getSchema.createClass(classOf[User])
        //
        //      val user1 = db.save(new User())


    }
}


