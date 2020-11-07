package moaclassifier;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import moa.core.Example;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * A transformation that wraps the classifier training it expexts data elements and emits clasifiers
 */
public class LearnProcessFunction extends ProcessFunction<Example<Instance>,Classifier>{
    //the class object that has to extend Classfier class of MOA
    Class<? extends Classifier> clazz;
    //member for the instantiated classifier that we will train
    Classifier classifier;
    //after how many data points do we want to send out a classifier into the stream
    private int updateFrequency;
    // counter for the arrived data examples
    private long numberOfCxampleSeen;

    // g
    public LearnProcessFunction(Class<? extends Classifier> clazz, int i) {
        this.clazz = clazz;
        this.updateFrequency = i;

    }
    @Override
    public void open(Configuration c) throws IllegalAccessException, InstantiationException {
        //instantiation of the classifier
        classifier = clazz.newInstance();
        // MOA initializer function for the classifier
        classifier.prepareForUse();

    }

    @Override
    public void processElement(Example<Instance> value, Context ctx, Collector<Classifier> out) throws Exception {
        numberOfCxampleSeen++;
        classifier.trainOnInstance(value);
        if(numberOfCxampleSeen % updateFrequency == 0)
            out.collect(classifier);

    }
}
