package moaclassifier;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import moa.classifiers.functions.NoChange;
import moa.core.Example;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * A CoFlatMapFunction for handling ConnectedStreams - that is multiple types (2) of stream elements and we execute different logic for different types.
 */
public class ClassifyAndUpdateFunction implements CoFlatMapFunction<Example<Instance>,Classifier,Boolean> {
    // the recent claasifier
    private Classifier classifier = new NoChange();
    //flatmap for handling Data elements
    @Override
    public void flatMap1(Example<Instance> value, Collector<Boolean> out) throws Exception {
        out.collect(classifier.correctlyClassifies(value.getData()));
    }
    //flatmap for handling Classifiers

    @Override
    public void flatMap2(Classifier value, Collector<Boolean> out) throws Exception {
        this.classifier = value;
    }
}
