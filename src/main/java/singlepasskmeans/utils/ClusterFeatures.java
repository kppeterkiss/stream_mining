package singlepasskmeans.utils;

public class ClusterFeatures {
    int n ;
    Point ls = new Point(0,0);
    Point ss = new Point(0,0);

    public void add(Point p){
        n++;
        ls.x+=p.x;
        ls.y+=p.y;

        ls.x+=Math.pow(p.x,2);
        ls.y+=Math.pow(p.y,2);
    }


}
