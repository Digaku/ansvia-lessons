package storm.starter.topology

import backtype.storm.{LocalDRPC, Config, LocalCluster, StormSubmitter}
import backtype.storm.drpc.LinearDRPCTopologyBuilder
import storm.starter.bolt.{AggregatorBolt, TextSplitterBolt, BasicExclamationBolt}
import backtype.storm.tuple.Fields
import backtype.storm.generated.DistributedRPC
import backtype.storm.utils.DRPCClient
import java.net.URL
import java.io.{InputStreamReader, BufferedReader, BufferedInputStream}

object DrpcClient {
    def main(args: Array[String]) {

        val drpc = new DRPCClient("localhost", 3772)

//        val rv1 = drpc.execute("exclamation", "hello robin is here")
//        println("rv1: " + rv1)

//        val uri = new URL("http://www.pimpworks.org/hp/relnotes/lg_files.txt")
        println("calculating data from: " + args(0) + "...")
        val uri = new URL(args(0))
        uri.openConnection()
        val is = new BufferedReader(new InputStreamReader(uri.openStream()))

        val largeData = Stream.continually(is.readLine())
          .takeWhile(_ != null).reduceLeftOption(_ + _).getOrElse("")

        println("calculating data size : " + largeData.length)

        val rv2 = drpc.execute("top-words", largeData)
        println("rv2: " + rv2)

        is.close()

    }
}
