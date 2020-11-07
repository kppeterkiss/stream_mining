package singlepasskmeans.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ClusteringFeature {
    int n;
    Point ls = new Point();
    Point ss = new Point();

    public void add(Point p){
        this.ls.x+=p.x;
        this.ls.y+=p.y;

        this.ss.x+=Math.pow(p.x,2);
        this.ss.y+=Math.pow(p.y,2);
        n++;

    }

    public Point getCentroid(){
        return new Point(this.ls.x/n, this.ls.y/n);
    }
    @Override
    public String toString(){
        return " [ cp ("+getCentroid().toString()+"), n "+this.n+",ls ("+round(this.ls.x,2)+" "+round(this.ls.y,2)+"), ss ("+round(this.ss.x,2)+" "+round(this.ss.y,2)+")]";
    }

    public double distace (Point p) {

        return getCentroid().euclideanDistance(p);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
