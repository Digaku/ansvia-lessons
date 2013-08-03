package example

import gen.thrift.trancam.{Ping, TrancamServer}
import org.apache.thrift.transport._
import org.apache.thrift.server.{TServer, TSimpleServer}
import org.apache.thrift.protocol.TBinaryProtocol
import gen.thrift.trancam.TrancamServer.Client


/**
 * Contoh ini melakukan bidirectional communication
 * antar server <-> client via Thrift.
 */
object TrancamBidi {

    case class Pinger(client:TrancamServer.Client, transport:TTransport) extends Thread {
        override def run() {
            var _stoped = false
            while (!_stoped){
                try {
                    client.ping(new Ping(System.currentTimeMillis().toInt))
                }catch{
                    case e:TTransportException =>
                        println("client connection loss")
                        transport.close()
                        _stoped = true
                }
                Thread.sleep(1000L)
            }
            println("thread down.")
        }
    }

    def main(args:Array[String]){

        val framedTransportFactory = new TFramedTransport.Factory()
        val protocolFactory = new TBinaryProtocol.Factory()

        val serverTransport = new TServerSocket(7366)

        serverTransport.listen()

        while (true){
            val transport = serverTransport.accept()
            if (transport != null){
                val useTransport = framedTransportFactory.getTransport(transport)

                val client = new Client(protocolFactory.getProtocol(useTransport))

                val rv = client.mul(7, 2)
                println("7 * 2 = " + rv)

                Pinger(client, transport).start()

//                transport.close()
            }
        }


    }

}
