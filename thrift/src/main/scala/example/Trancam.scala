package example

import gen.thrift.trancam.{Ping, TrancamServer}
import org.apache.thrift.transport.TServerSocket
import org.apache.thrift.server.{TServer, TSimpleServer}


object Trancam {


    class TrancamHandler extends TrancamServer.Iface {
        def ping(ts: Ping) {
           println("ping received. ts: " + ts.timestamp)
        }

        def mul(num1: Int, num2: Int) = {
            num1 * num2
        }
    }


    def main(args:Array[String]){

        val handler = new TrancamHandler
        val processor = new TrancamServer.Processor(handler)

        val trans = new TServerSocket(7366)
        val server = new TSimpleServer(new TServer.Args(trans).processor(processor))

        println("Listening at 7366...")
        server.serve()
    }

}
