package moaclassifier;

import com.yahoo.labs.samoa.instances.Instance;
import moa.core.Example;
import moa.streams.generators.RandomRBFGenerator;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;


public class ExampleSource extends RichSourceFunction<Example<Instance>> {

    private boolean isRunning = false;
    RandomRBFGenerator rrbf = new RandomRBFGenerator();

    @Override
    public void open(Configuration parameters) throws Exception{
        rrbf.prepareForUse();
    }

    @Override
    public void run(SourceContext<Example<Instance>> ctx) throws Exception {
        isRunning = true;
        while (isRunning)
            ctx.collect(rrbf.nextInstance());
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
