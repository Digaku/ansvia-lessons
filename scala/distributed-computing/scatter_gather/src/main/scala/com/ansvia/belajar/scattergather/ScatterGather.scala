

package com.ansvia.belajar.scattergather

import akka.actor._
import com.ansvia.commons.logging.Slf4jLogger
import akka.dispatch.{ExecutionContext, Future, Await, Promise}
import akka.pattern._
import akka.util.duration._
import akka.util.Timeout
import scala.util.continuations.cps


case class Message(msg:String)

class Recipient(id:Int) extends Actor {
    def receive = {
        case Message(msg) =>
            sender ! "[%d]: %s".format(id, msg)
    }
}

class Agregator(recipients:Iterable[ActorRef], _system:ActorSystem) extends Actor with Slf4jLogger {
    implicit val system = _system
    implicit val executer = ExecutionContext.defaultExecutionContext
    implicit val timeout:Timeout = 5 seconds

    def receive = {
        case msg @ Message(text) => {
            debug("Processing: " + text)

            val result = Promise[String]()
            val promises = List.fill(recipients.size)(Promise[String]())

            recipients.zip(promises).map{
                case (recipient, promise) => {
                    val rv = Await.result(recipient ? msg, timeout.duration).asInstanceOf[String]
                    Future.flow {
                        promise << rv
                    }
                }
            }

            Future.flow {
                def gather(promises: List[Future[String]], result: String = ""): String @cps[Future[Any]]  =
                    promises match {
                        case head :: tail => gather(tail, head() + result)
                        case Nil => result
                    }

                debug("binding result...")
                result << gather(promises)
            }

            sender ! result

        }

    }
}

object ScatterGather {

    implicit val timeout:Timeout = 5 seconds

    def main(args:Array[String]){
        implicit val system = ActorSystem()
        val recipients = (1 to 5) map(i => system.actorOf(Props(new Recipient(i))))
        val agregator = system.actorOf(Props(new Agregator(recipients, system)))

        Await.result(agregator ? Message("Hello"), timeout.duration).asInstanceOf[Promise[String]] map { rv1 =>
            println("rv1: " + rv1)
        }
        Await.result(agregator ? Message("Robin"), timeout.duration).asInstanceOf[Promise[String]] map { rv2 =>
            println("rv2: " + rv2)
        }

        recipients foreach(_ ! PoisonPill)
        system.shutdown()
    }

}