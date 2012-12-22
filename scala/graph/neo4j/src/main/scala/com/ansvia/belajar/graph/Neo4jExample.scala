package com.ansvia.belajar.graph

import org.neo4j.scala._
import sys.ShutdownHookThread
import org.neo4j.graphdb._
import org.neo4j.graphdb.Traverser.Order
import collection.JavaConversions

case class Matrix(name:String, profession:String)

object Neo4jExample extends Neo4jWrapper with EmbeddedGraphDatabaseServiceProvider {

    class TraverserIterator(t:Traverser){
        def foreach(c: Node => Unit){
            val it = t.iterator()
            while(it.hasNext){
                val n = it.next()
                c.apply(n)
            }
        }
        def foreachAs[T](c: Option[T] => Unit)(implicit evidence$1 : Manifest[T]){
            val it = t.iterator()
            while(it.hasNext){
                val n = it.next()
                c.apply(n.toCC[T])
            }
        }
    }

    implicit def traverserHelper(t:Traverser)=new TraverserIterator(t)

    def main(args:Array[String]){

        withTx {
            implicit neo =>
                val nodeMap = for ((name, prof) <- nodes) yield (name, createNode(Matrix(name, prof)))

                getReferenceNode --> "ROOT" --> nodeMap("Neo")

                val neoRel = nodeMap("Neo") --> "KNOWS" --> nodeMap("Trinity") <
                nodeMap("Neo") --> "KNOWS" --> nodeMap("Morpheus") --> "KNOWS" --> nodeMap("Trinity")
                nodeMap("Trinity") --> "KNOWS" --> nodeMap("Neo")
                nodeMap("Trinity") --> "LOVES" --> nodeMap("Neo") <-- "KNOWS" <-- nodeMap("Agent Smith")
                nodeMap("Morpheus") --> "KNOWS" --> nodeMap("Cypher") --> "KNOWS" --> nodeMap("Agent Smith")
                nodeMap("Agent Smith") --> "CODED_BY" --> nodeMap("The Architect")

                neoRel.update("boyfriend", true)

                /**
                 * Find the friends
                 */

                println("\n==== Find the friends of Neo ====")

                val nodeIt = nodeMap("Neo").traverse(Order.BREADTH_FIRST,
                    StopEvaluator.END_OF_GRAPH,
                    ReturnableEvaluator.ALL_BUT_START_NODE,
                    DynamicRelationshipType.withName("KNOWS"),
                    Direction.OUTGOING).iterator()

                while(nodeIt.hasNext){
                    val node = nodeIt.next()
                    node.toCC[Matrix] match {
                        case Some(Matrix(name, prof)) => println("%s is a %s".format(name, prof))
                        case None =>
                    }
                }

                println("\n=== Who is fall in love to Neo? ===")

                nodeMap("Neo").traverse(Order.BREADTH_FIRST,
                StopEvaluator.END_OF_GRAPH,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                DynamicRelationshipType.withName("LOVES"),
                Direction.INCOMING
                ).foreachAs[Matrix] { _ match {
                    case Some(Matrix(name, prof)) => println("%s is as %s".format(name, prof))
                    case None =>
                }
                }

                println("\n=== Who is knows Neo? ===")
                nodeMap("Neo").traverse(
                Order.BREADTH_FIRST,
                StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                DynamicRelationshipType.withName("KNOWS"),
                Direction.INCOMING
                ).foreachAs[Matrix](_ map { m => println("* %s as %s".format(m.name, m.profession)) })

                println("\n=== who is trinity's boyfriend? ===")
                val trinityName = "Trinity"
                nodeMap(trinityName).traverse(
                Order.BREADTH_FIRST,
                StopEvaluator.END_OF_GRAPH,
                new ReturnableEvaluator {
                    def isReturnableNode(currentPos: TraversalPosition) = {
                        currentPos.currentNode().getProperty("name") != trinityName &&
                        JavaConversions.iterableAsScalaIterable(currentPos.currentNode().getRelationships).count{ relat =>
                            relat("boyfriend").getOrElse(false)
                        } > 0
                    }
                },
                DynamicRelationshipType.withName("KNOWS"),
                Direction.INCOMING
                ).foreachAs[Matrix](_ map( m => println("* %s as %s".format(m.name, m.profession)) ))


        }
    }



    def neo4jStoreDir = "/tmp/neo4j-index"

    ShutdownHookThread {
        shutdown(ds)
    }

    final val nodes = Map(
    "Neo" -> "Hacker",
    "Morpheus" -> "Hacker",
    "Trinity" -> "Hacker",
    "Cypher" -> "Hacker",
    "Agent Smith" -> "Program",
    "The Architect" -> "Creator"
    )

}
