package com.ansvia.belajar

import akka._
import actor._
import dispatch.Await
import event._
import akka.pattern.{ask, pipe}
import util.duration._
import util.Timeout
import com.typesafe.config.ConfigFactory
/**
 * Author: robin
 * Date: 2/2/13
 * Time: 2:29 PM
 * 
 */
object RemoteExample {

    def main(args:Array[String]){

        val confStr =
            """
              |akka {
              |  actor {
              |    provider = "akka.remote.RemoteActorRefProvider"
              |  }
              |  remote {
              |    transport = "akka.remote.netty.NettyRemoteTransport"
              |    netty {
              |      hostname = "127.0.0.1"
              |      port = 2554
              |    }
              | }
              |}
            """.stripMargin.trim

        val conf = ConfigFactory.parseString(confStr)
        val system = ActorSystem("mindtalk", conf)

        val calc = system.actorFor("akka://mindtalk@127.0.0.1:2553/user/calculator")

        implicit val timeout = Timeout(5 seconds)
        val rv = calc ? ActorExample.Sum(5, 2)

        rv.onSuccess {
            case x:Int =>
                println(x)
        }

        println("done.")

    }
}
