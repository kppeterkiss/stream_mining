package singlepasskmeans;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import singlepasskmeans.utils.ClusterFeatures;
import singlepasskmeans.utils.Point;

import java.util.List;

public class KMeansProgram {

    public static void main(String[] args){

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Point> input = env.addSource(new PointSource());

        DataStream<List<ClusterFeatures>> clustering = input
                .keyBy(new KeySelector<Point, Long>()
                {


                    @Override
                    public Long getKey(Point value){
                        return 0L;
                    }
                })
                .countWindow(10).process(new SinglePassKMeans());

    }
}
