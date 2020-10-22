package bloomfilter;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BloomFilterProgram {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String>  input = env.readTextFile("src/main/java/bloomfilter/input")
        .keyBy(new KeySelector<String, Long>() {
            @Override
            public Long getKey(String value) throws Exception {
                return 0L;
            }
        }).process(new BloomFilter());

        input.writeToSocket("localhost", 5060, new SerializationSchema<String>() {
            @Override
            public byte[] serialize(String element) {
                return element.getBytes();
            }
        })  ;

        env.execute("bloomfilter");

    }



}
