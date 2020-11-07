package singlepasskmeans;


import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import singlepasskmeans.util.ClusteringFeature;
import singlepasskmeans.util.Point;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SimglePassKmeans extends ProcessWindowFunction<Point,List<ClusteringFeature>,Long, GlobalWindow> {

    private int k=3;

    private transient ValueState<List<ClusteringFeature>>  cfVs;

    @Override
    public void open(Configuration parameters){
        List<ClusteringFeature> cfs = new LinkedList<>();

        ValueStateDescriptor<List<ClusteringFeature>> cfdescriptor=new ValueStateDescriptor<List<ClusteringFeature>>("hf", TypeInformation.of(new TypeHint<List<ClusteringFeature>>() {
        }),cfs);
        cfVs =getRuntimeContext().getState(cfdescriptor);
    }
    @Override
    public void process(Long aLong, Context context, Iterable<Point> elements, Collector<List<ClusteringFeature>>out) throws Exception {
        List<ClusteringFeature> cfs = cfVs.value();

        if (cfs.size()==0){
            final List<Point> points = new LinkedList<>();
            elements.forEach(p->points.add(p));
            List<Integer> randomIndidces = getUniqueRandomInt(k,points.size());

            final List<Point> centroids = new LinkedList<>();
            randomIndidces.stream().forEach(i->centroids.add(points.get(i)));

            List<List<Point>>  clusters= new LinkedList<List<Point>>();
            for (Point p : centroids)
                clusters.add(new LinkedList<>());

            for (Point p:points){
                if (!centroids.contains(p)) {
                    int closest = 0;
                    double min_dist= centroids.get(0).euclideanDistance(p);
                    for (int i = 1; i < clusters.size(); ++i){
                        double act_dist = centroids.get(i).euclideanDistance(p);
                        if(min_dist>act_dist) {
                            min_dist = act_dist;
                            closest =i;
                        }
                    }

                    clusters.get(closest).add(p);
                }

            }


            for(int i = 0;i<centroids.size();++i){
                ClusteringFeature cf = new ClusteringFeature();
                cf.add(centroids.get(i));
                for(Point p : clusters.get(i))
                    cf.add(p);
                cfs.add(cf);
            }
        }
        else{
            for(Point p: elements){
                double min_dist = cfs.get(0).distace(p);
                int best = 0;
                for (int i=1;i<cfs.size();++i) {
                    double act_dist = cfs.get(i).distace(p);
                    if (act_dist<min_dist){
                        best=i;
                        min_dist=act_dist;
                    }
                }
                cfs.get(best).add(p);
            }

        }
        cfVs.update(cfs);

        out.collect(cfs);
    }

    public List<Integer> getUniqueRandomInt(int no, int max ){
        List<Integer> ret = new LinkedList<>();
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i=0; i<max; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i=0; i<no; i++) {
            ret.add(list.get(i));
        }
        return ret;
    }
    //https://stackoverflow.com/questions/8115722/generating-unique-random-numbers-in-java







}
