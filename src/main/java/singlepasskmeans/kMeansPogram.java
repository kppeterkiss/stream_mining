package singlepasskmeans;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import singlepasskmeans.util.ClusteringFeature;
import singlepasskmeans.util.Point;
import singlepasskmeans.util.PointSource;

import java.util.List;

public class kMeansPogram {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Point> input = env
                .addSource(new PointSource())
                .name("points");

        DataStream<List<ClusteringFeature>> clustering = input
                .keyBy(new KeySelector<Point, Long>() {
                    @Override
                    public Long getKey(Point value) throws Exception {
                        return 0L;
                    }
                })
                .countWindow(10).process(new SimglePassKmeans());



        DataStream<String> desc= clustering.map(new MapFunction<List<ClusteringFeature>, String>() {
            @Override
            public String map(List<ClusteringFeature> value) throws Exception {
               final String[] ret={""};
               value.stream().map(f->f.toString()).forEach(s->ret[0]+=s);
               return ret[0];
            }
        });
        desc.print();

        env.execute("");

    }

}
