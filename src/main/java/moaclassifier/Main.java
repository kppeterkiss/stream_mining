package moaclassifier;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.Example;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink program for training classifier
 */
public class Main {

    // Flink pipeline
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Example<Instance>> input = env.addSource(new ExampleSource());
        // tagging the elents using an OutputSelector
        SplitStream<Example<Instance>> trainAndTest = input.split(new RandomSamplingSelector(0.02))   ;
        // pick data with given tags
        DataStream<Example<Instance>> testStream = trainAndTest.select("test");
        DataStream<Example<Instance>> trainStream = trainAndTest.select(RandomSamplingSelector.TRAIN);
        // call a process function on the training data, that will train our classifier and emit time-by time  a new one
        // that is turning stream of training data into stream of classifiers
        DataStream<Classifier> classifiers = trainStream.process(new LearnProcessFunction(HoeffdingTree.class,1000));
        //make one stream from the testStram and classifier-stream, use the last seen classifier on the test data, then on  a window of 1000 element count how many times we made a good prediction.
        testStream.connect(classifiers).flatMap(new ClassifyAndUpdateFunction()).countWindowAll(1000).aggregate(new ParformanceFunction()).print();
        //execute the pipeline
        env.execute("");

    }

}
