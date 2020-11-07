package utilities;

public class BitMap {
    private int[] map;
    public BitMap(int n) {
        this.map = new int[n];
    }

    public void set(int pos) throws IndexOutOfBoundsException{
        this.map[pos] = 1;
    }

    public boolean get(int pos)throws IndexOutOfBoundsException{
        return this.map[pos]>0;
    }
    public void add(int pos){
        this.map[pos]++;
    }
    public int getValue(int pos){
        return this.map[pos];
    }

}
