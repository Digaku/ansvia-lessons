package storm.starter.topology;

import backtype.storm.LocalDRPC;
import backtype.storm.tuple.Fields;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.fluent.GroupedStream;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.FirstN;

/**
 * Author: robin
 * Date: 4/13/13
 * Time: 12:16 AM
 */
public class TridentDRPCHelper {
    private static TridentDRPCHelper ourInstance = new TridentDRPCHelper();

    public static TridentDRPCHelper getInstance() {
        return ourInstance;
    }

    private TridentDRPCHelper() {
    }

    public GroupedStream createStream(String name, TridentTopology topology, LocalDRPC drpc /*, TridentState state*/){
        return topology.newDRPCStream(name, drpc)
                .parallelismHint(16)
                .each(new Fields("args"), new Trident.Split(), new Fields("word"))
                .groupBy(new Fields("word"))
//                .stateQuery(state, new Fields("word"), new MapGet(), new Fields("count"))
//                .each(new Fields("count"), new FilterNull())
//                .persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"))
                ;
    }

    public Stream createTopWords(String name, TridentTopology topology, LocalDRPC drpc /*, TridentState state*/){
        return topology.newDRPCStream(name, drpc)
                .parallelismHint(16)
                .each(new Fields("args"), new Trident.Split(), new Fields("word"))
                .each(new Fields("word"), new Trident.StripNonKeyword())
                .groupBy(new Fields("word"))
                .aggregate(new Fields("word"), new Count(), new Fields("sum"))
                .aggregate(new Fields("word", "sum"), new FirstN.FirstNSortedAgg(10, "sum", true), new Fields("word", "sum"));
    }
}
