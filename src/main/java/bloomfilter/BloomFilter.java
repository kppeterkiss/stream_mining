package bloomfilter;

import apple.laf.JRSUIState;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BloomFilter extends KeyedProcessFunction<Long, String, String>{
    class BitMap{
        private int[] map;
        public BitMap(int n)
        {
            this.map = new int[n];

        }

        public void set(int pos){
            this.map[pos]=1;
        }

        public boolean get(int pos){
            return this.map[pos]>0;
        }
    }

    class SimpleHashFunction{

        // x-> (m*x+i)%m
        int multiplier;
        int increment;
        int modulo;

        public SimpleHashFunction(int b) {
            Random r = new Random();
            this.modulo = b;
            this.multiplier = r.nextInt(b);
            this.increment = r.nextInt(b);
        }

        int getIntValue(String s){
            int ans = 0;
            char[] ca = s.toCharArray();
            for (char c : ca)
                ans += c;
            return ans;
        }

        int getHash(String v){
            return (multiplier* getIntValue(v)+increment)%modulo;
        }
    }


    public void processElement(String input, Context context ,Collector<String> out) throws IOException {
        if(this.contains(input))
            return;
        this.add(input);
        out.collect(input+"\n");
    }
    int lenght = 12;
    int hash_no = 3;

    private transient ValueState<BitMap> bms;
    private transient ValueState<List<SimpleHashFunction>> hfs;


    @Override
    public   void open(Configuration conf){
        BitMap bm = new BitMap(lenght);
        List<SimpleHashFunction> hashes = new ArrayList<>();
        {
            for(int i =0;i<hash_no;++i)
                hashes.add(new SimpleHashFunction(hash_no));
        }
        ValueStateDescriptor<BitMap> decriptor = new ValueStateDescriptor<BitMap>("bm", TypeInformation.of(new TypeHint<BitMap>() {}),bm);
        ValueStateDescriptor<List<SimpleHashFunction>> shdecriptor = new ValueStateDescriptor<List<SimpleHashFunction>>("shf", TypeInformation.of(new TypeHint<List<SimpleHashFunction>>() {}),hashes);


        bms=getRuntimeContext().getState(decriptor);
        hfs = getRuntimeContext().getState(shdecriptor);


    }




    public void add(String v) throws IOException {
        BitMap bm = bms.value();
        List<SimpleHashFunction> hashes = hfs.value();
        for (SimpleHashFunction h : hashes)
            bm.set(h.getHash(v));

        bms.update(bm);
    }

    private boolean contains(String input) throws IOException {
        BitMap bm = bms.value();
        List<SimpleHashFunction> hashes = hfs.value();
        int counter = 0;
        for (SimpleHashFunction h : hashes)
            if (bm.get(h.getHash(input)))
                counter++;
        return counter==this.hash_no;
    }

}
