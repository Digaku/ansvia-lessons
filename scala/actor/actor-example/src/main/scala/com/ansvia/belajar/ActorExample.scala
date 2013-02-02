package com.ansvia.belajar

import akka._
import actor._
import dispatch.Await
import event._
import akka.pattern.{ask, pipe}
import util.duration._
import util.Timeout
import com.typesafe.config.ConfigFactory

object ActorExample {

    case class Sum(a:Int, b:Int)

    class CalculatorActor extends Actor {
        def receive = {
            case "hello" =>
                println("hello juga")

            case Sum(a, b) =>
                val result = a + b
//                Thread.sleep(5000)
                println("result: " + result)
                println("sender: " + sender)
                sender ! result
        }
    }


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
              |      port = 2553
              |    }
              | }
              |}
            """.stripMargin.trim

        val conf = ConfigFactory.parseString(confStr)
        val system = ActorSystem("mindtalk", conf)

        val calc = system.actorOf(Props[CalculatorActor], name = "calculator")
//
        calc ! "hello"
//        calc ! Sum(2, 4)
//
//        implicit val timeout = Timeout(5 seconds)
//
//        val result = calc ? Sum(5, 5)
////        val rv = Await.result(result, timeout.duration)
//
//        result.onSuccess {
//            case x:Int =>
//                println("rv: " + x)
//        }
//
//
//        println("done")
    }

}



