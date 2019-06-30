package br.com.douglas444.common;

import java.util.List;

public class Cluster {

    private List<Point> points;

    public Cluster(List<Point> points) {
        this.points = points;
    }

    /** Calculates center of the cluster.
     *
     * @return the center os the cluster.
     */
    public Point calculateCenter() {

        Point center = null;

        if (this.points.size() > 0) {
            center = this.points.get(0).copy();
            this.points.subList(1, this.points.size()).forEach(center::sum);
            center.divide(this.points.size());
        }

        return center;

    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
