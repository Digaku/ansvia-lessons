package com.ansvia.belajar.graph

import models.User
import scala.collection.JavaConversions._
import com.orientechnologies.orient.`object`.db.OObjectDatabaseTx
import com.orientechnologies.orient.core.db.graph.OGraphDatabase
import com.orientechnologies.orient.core.metadata.schema.{OType, OClass}
;
import com.ansvia.perf.PerfTiming
import com.orientechnologies.orient.core.exception.OSchemaException
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.record.impl.ODocument

object OrientdbObjectExample extends PerfTiming {

//    import com.ansvia.perf.Perf.timing
    import Implicits._


    def main(args:Array[String]){

//        val uri = "memory:OrientdbObjectExample"
        val uri = "remote:localhost/temp"
        //        val uri = "memory:OrientdbObjectExample"
        //        val orient = Orient.instance()
        val dbx = new OObjectDatabaseTx(uri)
        implicit val db:OObjectDatabaseTx = dbx.open("admin", "admin")

        // di remote mode tidak bisa create database
        // harus melalu config file.
//        implicit val db:OObjectDatabaseTx =
//            if (dbx.exists())
//                dbx.open("admin", "admin")
//            else{
//                dbx.create()
//            }

        //        db.getMetadata.getSchema.createClass(classOf[User])
//        db.getEntityManager.registerEntityClass(classOf[ODocument])
        db.getEntityManager.registerEntityClass(classOf[User])
        db.getEntityManager.registerEntityClasses("com.orientechnologies.orient.core.metadata.schema")

        val uclass = db.getMetadata.getSchema.getClass("User")
        uclass.createProperty("name", OType.STRING)
        uclass.createIndex("nameIndex", OClass.INDEX_TYPE.UNIQUE, "name")

        val gondez = new User("gondez")
        val robin = new User("robin")
        val temon = new User("temon")
        val adit = new User("adit")

//        robin.supporting += gondez
//        robin.supporting += temon
//        temon.supporting += gondez

        db.save(gondez)
        db.save(temon)
        db.save(robin)
        db.save(adit)

        println("count class elements: " + db.countClusterElements("User"))

        var result: List[User] = null

        timing("get from sql like query"){
            result = db.queryBySql[User]("select * from User")
            for ( u <- result ){
                if (u.name == "gondez"){
                    u.supporting += adit
                    u.save()
                }
                if (u.name == "robin")
                {
                    u.supporting += gondez
                    u.save()
                }
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


        timing("Search using traverse"){
            result = db.queryBySql("select * from User where name='%s'".format(gondez.name))
            if(result.length > 0){
                result = db.queryBySql[User]("traverse supporting from #" + result.head.id)

                for (u <- result){
                    println(" + " + u)
                }
            }
        }

        timing("get traverse path"){
            val rv = db.queryBySql[User]("traverse in,out from User while $depth <= 5")
            for( z <- rv ){
                println(" + " + z)
            }
        }

        timing("test from remote"){
            val db2 = new OGraphDatabase("remote:localhost/temp")
            db2.open("admin", "admin")

            try {
                db2.createVertexType("TestUser")
            }catch{
                case e:OSchemaException =>
                    println("not creating vertex schema: " + e.getMessage)
            }

            val result = db2.queryBySql[ODocument]("select * from TestUser where name='robin_remote'")

            for (u <- result){
                println(" + " + u)
            }


            val v1 = db2.createVertex("TestUser")
            v1.field("name", "robin_remote")
            db2.save(v1)

            val rv = db2.browseVertices()

            for (u <- rv){
                println(" + " + u.field("name"))
            }

            v1.delete()
            db2.close()
        }


        timing("Who is user supported by robin?"){
            val result = db.queryBySql[User]("select * from User where name='robin'")
            for(u <- result) {
                println(" + %s supporting %d users".format(u.name, u.supporting.size()))
                u.supporting.foreach(u => println(" * " + u.toString))
            }
        }

        timing("get supporting of supporting to adit"){
            val result = db.queryBySql[User]("select * from User where supporting.supporting contains (name='adit')")
            result.foreach(u => println(" + " + u))
        }

        timing("who is adit's supporters?"){
            val result = db.queryBySql[User]("select * from User where supporting contains (name='adit')")
            result.foreach(u => println(" + " + u))
        }


        // di remote mode database dihandle oleh config
        // jadi gak bisa drop database via client.
//        db.drop()
        // remote mode bisa hapus cluster atau schema class.
        db.getMetadata.getSchema.dropClass("User")
        db.close()

        //      val oclass = db.getMetadata.getSchema.createClass(classOf[User])
        //
        //      val user1 = db.save(new User())


    }
}


