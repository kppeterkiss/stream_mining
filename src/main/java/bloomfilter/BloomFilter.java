package bloomfilter;

import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BloomFilter {
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

    public void processElement(String input, Collector<String> out){
        if(this.contains(input))
            return;
        this.add(input);
        out.collect(input);
    }
    int lenght = 12;
    int hash_no = 3;

    BitMap bm = new BitMap(lenght);
    List<SimpleHashFunction> hashes = new ArrayList<>();
    {
        for(int i =0;i<hash_no;++i)
            hashes.add(new SimpleHashFunction(hash_no));
    }


    public void add(String v){
        for (SimpleHashFunction h : this.hashes)
            this.bm.set(h.getHash(v));
    }

    private boolean contains(String input) {
        int counter = 0;
        for (SimpleHashFunction h : this.hashes)
            if (this.bm.get(h.getHash(input)))
                counter++;
        return counter==this.hash_no;
    }

}
