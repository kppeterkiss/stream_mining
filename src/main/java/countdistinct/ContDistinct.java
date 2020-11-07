package countdistinct;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.BitMap;
import utilities.SimpleHashFunction;


public class ContDistinct extends KeyedProcessFunction<Long,String,Integer> {


    transient ValueState<BitMap> bm;
    transient ValueState<SimpleHashFunction> hashFunction;

    private static final Logger Log = LoggerFactory.getLogger(ContDistinct.class);

    int bitmap_lenght= 12;

    @Override
    public void open(Configuration config){
        ValueStateDescriptor<BitMap> bmd= new ValueStateDescriptor<BitMap>("bm", TypeInformation.of(new TypeHint<BitMap>(){}),new BitMap(bitmap_lenght));
        ValueStateDescriptor<SimpleHashFunction> shfd = new ValueStateDescriptor<SimpleHashFunction>("sh",TypeInformation.of(new TypeHint<SimpleHashFunction>() {
        }),new SimpleHashFunction(bitmap_lenght));
        bm = getRuntimeContext().getState(bmd);
        hashFunction = getRuntimeContext().getState(shfd);
    }

    public int getTailLength(String binString){
        char[] ca = binString.toCharArray();
        int tailingZeros = 0;
        for (int i= ca.length-1;i>=0;i--)
            if(ca[i]=='0')
                tailingZeros++;
        if (tailingZeros==ca.length) return 0;
        return tailingZeros;
    }

    public int getR(BitMap bm){
        int i = 0;
        while (bm.get(i)) i++;
        return (int)Math.pow(2,i);
    }

    @Override
    public void processElement(String value, Context ctx, Collector<Integer> out) throws Exception {
        BitMap bm_ = bm.value();
        Log.info("String ",value );
        System.out.println(value);
        SimpleHashFunction hashFunction_= hashFunction.value();
        String binary = Integer.toBinaryString(hashFunction_.getHash(value));
        bm_.set(getTailLength(binary));

        int estimate = getR(bm_);
        Log.info("Estimate",estimate );
        out.collect(Integer.valueOf(estimate));


    }
}
