package br.com.douglas444.common;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Point> {

    private Point target;

    public DistanceComparator(Point target) {
        this.target = target;
    }

    /** Compares the distances of the two points passed as argument to the
     * the target point defined as a class attribute.
     *
     * @return Returns 0 if p1 and p2 have the same distance to the target
     * point, -1 if p1 are closer to the target point and returns 1 if p1
     * are closer to the target point.
     */
    @Override
    public int compare(Point p1, Point p2) {

        double d1 = p1.distance(this.target);
        double d2 = p2.distance(this.target);

        return Double.compare(d1, d2);
    }

    public Point getTarget() {
        return target;
    }

    public void setTarget(Point target) {
        this.target = target;
    }

}
