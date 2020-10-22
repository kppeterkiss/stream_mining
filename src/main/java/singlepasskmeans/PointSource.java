package singlepasskmeans;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import singlepasskmeans.utils.Point;

public class PointSource implements SourceFunction<Point> {
    @Override
    public void run(SourceContext<Point> ctx) throws Exception {

    }

    @Override
    public void cancel() {

    }
}
