package moaclassifier;

import com.yahoo.labs.samoa.instances.Instance;
import moa.core.Example;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;

import java.util.Collections;
import java.util.Random;

public class RandomSamplingSelector implements OutputSelector<Example<Instance>> {

    public static  String  TEST="test";
    public static  String  TRAIN="train";

    public final Iterable<String> TEST_LIST = Collections.singletonList(TEST);
    public final Iterable<String> TRAIN_LIST = Collections.singletonList(TRAIN);

    private double split;
    private Random r = new Random();

    public RandomSamplingSelector(double v) {
        this.split = v;
    }

    @Override
    public Iterable<String> select(Example<Instance> value) {

        if(r.nextFloat()>split)
            return TRAIN_LIST;
        return TEST_LIST;
    }
}
