package countminsketch;

import utilities.BitMap;
import utilities.SimpleHashFunction;

public class CMSRow {

    BitMap bm;
    SimpleHashFunction hash;

    public CMSRow(int n) {
        this.bm = new BitMap(n);
        this.hash = new SimpleHashFunction(n);
    }


    public void hash(String value){
        this.bm.add(this.hash.getHash(value));
    }

    public int query(String value){
        return  this.bm.getValue(this.hash.getHash(value));
    }

}
