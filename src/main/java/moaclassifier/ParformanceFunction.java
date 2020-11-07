package moaclassifier;

import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.AggregateFunction;


/**
 *
 *
 * Calculate the accuracy of the predictor, receiving a stream of true or false corresponding to the fact that
 * expects input type an accumulator type and an ooutput type
 * th accumulator can be thought of as an intermediate aggregator
 */
public class ParformanceFunction implements AggregateFunction<Boolean,AverageAccumulator,Double>{
    @Override
    public AverageAccumulator createAccumulator() {
        return new AverageAccumulator();
    }

    @Override
    public AverageAccumulator add(Boolean value, AverageAccumulator accumulator) {
        accumulator.add(value?1:0);
        return accumulator;
    }

    @Override
    public Double getResult(AverageAccumulator accumulator) {
        return accumulator.getLocalValue();
    }

    @Override
    public AverageAccumulator merge(AverageAccumulator a, AverageAccumulator b) {
        a.merge(b);
        return a;
    }
}
