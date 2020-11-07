package countminsketch;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class CMSProgram {



    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        DataStream<Integer> input = env.readTextFile("src/main/resources/input")
                .keyBy(new KeySelector<String, Long>() {
                    @Override
                    public Long getKey(String value) throws Exception {
                        return 0L;
                    }
                }).process(new CMS());

        input.addSink(new SinkFunction<Integer>() {
            @Override
            public void invoke(Integer value) throws Exception {
                //log.info(String.valueOf(value))  ;
               // System.out.println(value);
            }
        });

        env.execute("count distinct");
    }

}
