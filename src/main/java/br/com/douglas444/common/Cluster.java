package br.com.douglas444.common;

import java.util.List;

public class Cluster {

    private String category;
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

    public double calculateStandardDeviation() {

        Point center = this.calculateCenter();

        double sum = this
                .points
                .stream()
                .mapToDouble(point -> Math.pow(point.distance(center), 2))
                .sum();

        return Math.sqrt(sum / this.points.size());
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
