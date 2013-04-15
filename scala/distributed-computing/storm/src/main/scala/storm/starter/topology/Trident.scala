package storm.starter.topology

import storm.trident.operation.{BaseFilter, TridentCollector, BaseFunction}
import storm.trident.tuple.TridentTuple
import backtype.storm.tuple.{Fields, Values}
import backtype.storm.{LocalCluster, StormSubmitter, Config, LocalDRPC}
import backtype.storm.generated.StormTopology
import storm.trident.TridentTopology
import storm.trident.operation.builtin._

/**
 * Author: robin
 * Date: 4/12/13
 * Time: 11:54 PM
 *
 */
object Trident {

    class Split extends BaseFunction {
        def execute(d: TridentTuple, col: TridentCollector) {
            val sentence = d.getString(0)
            for (word <- sentence.split("\\W+"))
                col.emit(new Values(word.toLowerCase.trim))
        }
    }

    final val EXCLUDED = Seq("the","a","is","am","are",
      "of","it","to","as","and","that",
      "be","this","in","at","on","or","when",
      "from","yet","not","nor","if","their","these",
      "your","his","i","you","for","can","them","have","has",
      "will")

    class StripNonKeyword extends BaseFilter {
      def isKeep(tuple:TridentTuple):Boolean={
        !EXCLUDED.contains(tuple.getString(0))
      }
    }


    def buildTopology(drpc:LocalDRPC):StormTopology = {
//        val spout = new FixedBatchSpout(new Fields("sentence"), 3,
//            new Values("the cow jumped over the moon"),
//            new Values("the man went to the store and bought some candy"),
//            new Values("four score and seven years ago"),
//            new Values("how many apples can you eat"),
//            new Values("to be or not to be the person"))
//        spout.setCycle(true)

        val trident = new TridentTopology()

//        val wordCounts = trident.newStream("spout1", spout)
//            .parallelismHint(16)
//            .each(new Fields("sentence"), new Split(), new Fields("word"))
//            .groupBy(new Fields("word"))
//            .persistentAggregate(new MemoryMapState.Factory, new Count(), new Fields("count"))
//            .parallelismHint(16)

        TridentDRPCHelper.getInstance().createStream("words", trident, drpc /*, wordCounts*/)
          .aggregate(new Fields("word"), new Count(), new Fields("sum"))

        TridentDRPCHelper.getInstance().createTopWords("top-words", trident, drpc)


        trident.build()
    }

    def main(args: Array[String]) {
        val conf = new Config()
        conf.setMaxSpoutPending(20)

        if (args.length == 0){
            val drpc = new LocalDRPC()
            val cluster = new LocalCluster()
            cluster.submitTopology("wordCounter", conf, buildTopology(drpc))
            for(i <- (0 to 100)) {
                println("DRPC RESULT: " + drpc.execute("words", "cat the dog jumped over the lazy fox"))
                Thread.sleep(1000)
            }
        }else{
            conf.setNumWorkers(3)
//            conf.setMaxSpoutPending(1000)
            conf.setNumAckers(0)
//            conf.setMaxTaskParallelism(3)
            StormSubmitter.submitTopology(args(0), conf, buildTopology(null))
        }
    }
}