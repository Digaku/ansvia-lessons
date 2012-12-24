package com.ansvia.belajar.graph

import org.neo4j.scala._
import sys.ShutdownHookThread
import org.neo4j.graphdb._
import org.neo4j.graphdb.Traverser.Order
import collection.JavaConversions

case class Matrix(name:String, profession:String)
case class User(name:String, fullName:String, location:String)

object Neo4jExample extends Neo4jWrapper with EmbeddedGraphDatabaseServiceProvider {

    class TraverserIterator(t:Traverser){
        def foreach(c: Node => Unit){
            val it = t.iterator()
            while(it.hasNext){
                val n = it.next()
                c.apply(n)
            }
        }
        def foreachAs[T](c: T => Unit)(implicit evidence$1 : Manifest[T]){
            val it = t.iterator()
            while(it.hasNext){
                val n = it.next()
                n.toCC[T] match {
                    case Some(x) => c.apply(x)
                    case None =>
                }
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
                ).foreachAs[Matrix] { x =>  println("%s is as %s".format(x.name, x.profession)) }

                println("\n=== Who is knows Neo? ===")
                nodeMap("Neo").traverse(
                Order.BREADTH_FIRST,
                StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                DynamicRelationshipType.withName("KNOWS"),
                Direction.INCOMING
                ).foreachAs[Matrix](m => println("* %s as %s".format(m.name, m.profession)))

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
                ).foreachAs[Matrix](m => println("* %s as %s".format(m.name, m.profession)))


        }


        withTx {
            implicit neo =>
            {
                val gondez = createNode(User("gondez", "Cahyanto Kurniawan", "Indonesia/Jawa Tengah/Solo"))
                val robin = createNode(User("robin", "Robin", "Indonesia/Jawa tengah/Wonosobo"))
                val temon = createNode(User("temon", "Eko Prasetyo", "Indonesia/Yogyakarta/Kota"))
                val anis = createNode(User("anis", "Anis", "Indonesia/Jawa tengah/Wonosobo"))
                val danny = createNode(User("Danny", "Danny Oei", "Indonesia/Jakarta/Pusat"))
                val giring = createNode(User("Giring", "Giring Ganesha", "Indonesia/Jakarta/Pusat"))
                val aisha = createNode(User("Aisha", "Aisha", "Indonesia/Jakarta/Pusat"))


                gondez --> "SUPPORT" --> temon
                temon --> "SUPPORT" --> robin
                robin --> "SUPPORT" --> gondez
                robin --> "SUPPORT" --> anis
                temon --> "SUPPORT" --> anis
                robin --> "SUPPORT" --> danny
                danny --> "SUPPORT" --> giring
                gondez --> "SUPPORT" --> giring
                giring --> "SUPPORT" --> aisha
                temon --> "SUPPORT" --> aisha


                println("\n======= Who is support robin?")

                robin.traverse(
                Order.BREADTH_FIRST, StopEvaluator.DEPTH_ONE, ReturnableEvaluator.ALL_BUT_START_NODE,
                DynamicRelationshipType.withName("SUPPORT"), Direction.INCOMING
                ).foreachAs[User]{
                    u =>
                        println(" * %s (%s) from %s".format(u.name, u.fullName, u.location))
                }


            }
        }


        withTx {
            implicit neo =>
                getAllNodes.foreach {
                    n => printPYMK(n)
                }
        }
    }

    def printPYMK(node:Node){
        println("\n======= User may %s know:".format(node.getProperty("name")))
        node.traverse(
            Order.BREADTH_FIRST,
            new StopEvaluator {
                def isStopNode(currentPos: TraversalPosition) = {
                    currentPos.depth() > 2
                }
            },
            new ReturnableEvaluator {
                def isReturnableNode(currentPos: TraversalPosition) = {
                    val innerNode = currentPos.currentNode()
                    val supports = JavaConversions.iterableAsScalaIterable(innerNode.getRelationships(DynamicRelationshipType.withName("SUPPORT"),Direction.INCOMING))
                    !(innerNode.equals(node) || supports.count(_.getStartNode.equals(node)) > 0)
                }
            }
            ,
            DynamicRelationshipType.withName("SUPPORT"), Direction.OUTGOING
        ).foreach{ n => n.toCC[User].map { u =>
            print(" * %s (%s) from %s".format(u.name, u.fullName, u.location))
            val supports = n.traverse(
                Order.BREADTH_FIRST,
                new StopEvaluator {
                    def isStopNode(currentPos: TraversalPosition) = {
                        currentPos.depth() > 2
                    }
                },
                new ReturnableEvaluator {
                    def isReturnableNode(currentPos: TraversalPosition) = {
                        JavaConversions.iterableAsScalaIterable(currentPos.currentNode().getRelationships(DynamicRelationshipType.withName("SUPPORT"), Direction.INCOMING))
                                .count { n =>
                            n.getStartNode.equals(node)
                        } > 0
                    }
                },
                DynamicRelationshipType.withName("SUPPORT"),
                Direction.INCOMING
            )
            val nodes = supports.getAllNodes
            if (nodes.size() > 0){
                print(" supported by: ")
                JavaConversions.collectionAsScalaIterable(nodes).foreach{ n =>
                    print(n.getProperty("name") + ",")
                }
                println("")
            }else{
                println("")
            }
        }}
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
