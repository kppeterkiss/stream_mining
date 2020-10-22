package singlepasskmeans;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import singlepasskmeans.utils.ClusterFeatures;
import singlepasskmeans.utils.Point;

import java.util.LinkedList;
import java.util.List;

public class SinglePassKMeans extends ProcessWindowFunction<Point,List<ClusterFeatures>,Long,GlobalWindow> {


    private int k=3;

    private transient ValueState<List<ClusterFeatures>> cfss;


    @Override
    public void open(Configuration conf){
        List<ClusterFeatures> cfs = new LinkedList<>();

        ValueStateDescriptor<List<ClusterFeatures>> cfsdesc = new ValueStateDescriptor<List<ClusterFeatures>>("hf", TypeInformation.of(new TypeHint<List<ClusterFeatures>>() {
        }),cfs);

        cfss = getRuntimeContext().getState(cfsdesc);

    }


    @Override
    public void process(Long aLong, Context context, Iterable<Point> elements, Collector<List<ClusterFeatures>> out) throws Exception {
        List<ClusterFeatures> cfs = cfss.value();
        if(cfs.size()==0){

            // read all points
            // create an initial clustering
            //created clusters store into clusterfeatures
        }
        else{
            //point by point
            //check what CF is closest
            //add to it

        }

        cfss.update(cfs);



    }
}
