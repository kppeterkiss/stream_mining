package singlepasskmeans.util;

/**
 * A simple two-dimensional centroid, basically a point with an ID.
 */
public  class Centroid_ extends Point {

    public int id;

    public Centroid_() {}

    public Centroid_(int id, double x, double y) {
        super(x, y);
        this.id = id;
    }

    public Centroid_(int id, Point p) {
        super(p.x, p.y);
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " " + super.toString();
    }
}