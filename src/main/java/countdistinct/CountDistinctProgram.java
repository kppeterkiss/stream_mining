package countdistinct;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountDistinctProgram {

    static final Logger log = LoggerFactory.getLogger(CountDistinctProgram.class);
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        DataStream<Integer> input = env.readTextFile("src/main/resources/input")
                .keyBy(new KeySelector<String, Long>() {
                    @Override
                    public Long getKey(String value) throws Exception {
                        return 0L;
                    }
                }).process(new ContDistinct());

        input.addSink(new SinkFunction<Integer>() {
            @Override
            public void invoke(Integer value) throws Exception {
                log.info(String.valueOf(value))  ;
                //System.out.println(value);
            }
        });

        env.execute("count distinct");


    }

}
