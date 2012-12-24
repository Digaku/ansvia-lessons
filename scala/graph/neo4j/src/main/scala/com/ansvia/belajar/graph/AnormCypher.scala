package com.ansvia.belajar.graph

import org.anormcypher._

/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/24/12
 * Time: 7:13 PM
 * 
 */
object AnormCypher {

    def main(args:Array[String]){

        Neo4jREST.setServer("localhost", 7474)


        val users = Array(
        User("robin","Robin Sy.", "Indonesia/Jawa tengah/Wonosobo"),
        User("gondez","Cahyanto Kurniawan", "Indonesia/Jawa tengah/Solo"),
        User("giring","Giring Ganesha", "Indonesia/Jakarta/Pusat"),
        User("danny","Danny Wirianto", "Indonesia/Jakarta/Pusat")
        )

//        Cypher("""create (anorm {name:"AnormCypher"}), (test {name:"Test"})""").execute()
        val statement =
            """create %s""".format {
                val cyp =
                    users.map { u =>
                        "(%s {name:\"%s\",fullName:\"%s\",location:\"%s\"})".format(u.name, u.name, u.fullName, u.location)
                    }.reduce(_ + "," + _)
                cyp
            }
        println("statement: " + statement)
        Cypher(statement).execute()

        val stream = Cypher("START n=node(*) WHERE HAS(n.name) AND HAS(n.fullName) AND HAS(n.location) RETURN n.name, n.fullName, n.location;")()
        val userList = stream.map { row =>
            User(row[String]("n.name"),row[String]("n.fullName"),row[String]("n.location"))
        }

        for (u <- userList){
            println(" + %s".format(u))
        }

    }
}
