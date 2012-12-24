package com.ansvia.belajar.graph

import org.neo4j.scala._
import sys.ShutdownHookThread
import org.neo4j.graphdb._
import org.neo4j.graphdb.Traverser.Order
import collection.JavaConversions

/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/24/12
 * Time: 1:39 PM
 * 
 */
//object Implicits {
//
//    class TraverserIterator(t:Traverser){
//        def foreach(c: Node => Unit){
//            val it = t.iterator()
//            while(it.hasNext){
//                val n = it.next()
//                c.apply(n)
//            }
//        }
//        def foreachAs[T](c: Option[T] => Unit)(implicit evidence$1 : Manifest[T]){
//            val it = t.iterator()
//            while(it.hasNext){
//                val n = it.next()
//                c.apply(toCC[T](n))
//            }
//        }
//    }
//
//    implicit def traverserHelper(t:Traverser)=new TraverserIterator(t)
//
//}
