package br.com.douglas444.samknn.internal;

import br.com.douglas444.dsframework.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class STM extends Memory {

    /** Returns the bisection containing the most recent samples.
     *
     * @return a list with the most recent samples.
     */
    private List<Point> getMostRecentBisection() {

        List<Point> set = super.getPoints();
        return set.subList(set.size()/2, set.size());

    }

    /** Resets the memory, deleting all points, and then retrains the model
     * inserting one by one the points of the sequence passed as argument.
     *
     * @param sequence list of points representing the sequence.
     * @return the Interleaved Test-Train Error.
     */
    private double testTrainOnSequence(List<Point> sequence) {

        int losses = 0;
        super.setPoints(new ArrayList<>());

        for (Point point : sequence) {
            Optional<Double> label = super.predict(point);
            if (label.isPresent() && label.get() != point.getY()) {
                ++losses;
            }
            super.insert(point);
        }

        return (double) losses / sequence.size();

    }

    /** Shrunk the memory size, sliding the dynamic window to the most recent
     * points such that the Interleaved Test-Train error is minimized.
     *
     * @return a list with the discarded points.
     */
    List<Point> shrunk() {

        STM minimum = new STM();
        double minimumITTE = minimum.testTrainOnSequence(this.getMostRecentBisection());

        STM bisection = new STM();
        double bisectionITTE = bisection.testTrainOnSequence(minimum.getMostRecentBisection());


        while (bisection.size() > Hyperparameter.L_MIN) {

            if (bisectionITTE < minimumITTE) {
                minimumITTE = bisectionITTE;
                minimum = bisection;
            }

            bisectionITTE = bisection.testTrainOnSequence(bisection.getMostRecentBisection());

        }

        List<Point> discardedPoints = super.getPoints().subList(0,
                super.size() - minimum.size() - 1);

        super.setPoints(minimum.getPoints());
        return discardedPoints;

    }

}
