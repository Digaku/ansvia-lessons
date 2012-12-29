package com.ansvia.belajar.graph

import com.orientechnologies.orient.`object`.db.OObjectDatabaseTx
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import models.DigakuModel
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.db.graph.OGraphDatabase
import com.orientechnologies.orient.core.query.OQuery
import com.orientechnologies.orient.core.sql.query

/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/27/12
 * Time: 12:52 AM
 * 
 */
object Implicits {

    /**
     * database wrapper with auto detach on query result record.
     * @param db database object.
     * @return
     */
    implicit def dbWrapper(db: OObjectDatabaseTx) = new {
        def queryBySql[T](sql: String, params: AnyRef*): List[T] = {
            val params4java = params.toArray
            val results: java.util.List[T] = db.query(new OSQLSynchQuery[T](sql), params4java: _*)
            for ( rv <- results.asScala.toList )
            yield {
                db.detach(rv)
                rv
            }
        }
    }

    implicit def dbWrapperGraph(db:OGraphDatabase) = new {
        def queryBySql[T <: AnyRef](sql:String):List[T] = {
            val results: java.util.List[T] = db.query(new OSQLSynchQuery[T](sql))
            for ( rv <- results.asScala.toList )
                yield {
                    rv
                }
        }
    }


    /**
     * wrap iterable java List to override it IterableLike.foreach to our own
     * for auto detach
     * @param lst list to wrap.
     * @param db database in implicit.
     * @tparam T class type must be subclass from DigakuModel.
     * @return
     */
    implicit def listWrapper[T <: DigakuModel](lst:java.util.List[T])(implicit db:OObjectDatabaseTx) = new {
        def foreach(func: T => Unit){
            lst.iterator().foreach { d =>
                db.detach(d)
                func.apply(d)
            }
        }
    }

}
