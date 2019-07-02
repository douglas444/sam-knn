package br.com.douglas444.common;

import java.util.Arrays;
import java.util.Objects;

public class Point {

    private int t;
    private double[] x;
    private double y;

    public Point(int t, double[] x, double y) {
        this.t = t;
        this.x = x;
        this.y = y;
    }

    public Point(double[] x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(y);
        result = 31 * result + Arrays.hashCode(x);
        return result;
    }

    /** Calculates the distance of this point to another.
     *
     * @param point the point to which the distance will be calculated.
     * @return the calculated distance.
     */
    public double distance(Point point) {
        double sum = 0;
        for (int i = 0; i < point.getX().length; ++i) {
            sum += (point.getX()[i] - this.getX()[i]) * (point.getX()[i] - this.getX()[i]);
        }
        return Math.sqrt(sum);
    }

    /** Indicates whether the coordinates x and y of some other point is
     * "equal to" this one.
     *
     * @param o the reference to the object to be compared
     * @return true if the coordinates x and y are the same as the point
     * passed as argument; false otherwise or if the object passed isn't
     * a instance of br.com.douglas444.common.Point class
     */
    @Override
    public boolean equals(Object o) {

        if (o.getClass() != Point.class) {
            return false;
        }

        Point point = (Point) o;

        for (int i = 0; i < point.getX().length; ++i) {
            if (point.getX()[i] != this.x[i]) {
                return false;
            }
        }

        return point.getY() == this.y;

    }

    /** Sum coordinates x of another point to this one. Coordinate y keeps the same.
     *
     * @param point the point to be added.
     */
    public void sum(Point point) {

        for (int i = 0; i < x.length; ++i) {
            x[i] += point.getX()[i];
        }

    }

    /** Divide each coordinate of x by a scalar.
     *
     * @param scalar the scalar.
     */
    public void divide(double scalar) {

        for (int i = 0; i < x.length; ++i) {
            x[i] /= scalar;
        }

    }
    /** Return a copy of the point, with no reference between then.
     *
     * @return a copy of the point.
     */
    public Point copy() {

        Point point = new Point(new double[x.length], y);
        for (int i = 0; i < x.length; ++i) {
            point.getX()[i] = x[i];
        }
        return point;

    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
