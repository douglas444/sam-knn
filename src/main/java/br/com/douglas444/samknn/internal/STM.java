package br.com.douglas444.samknn.internal;

import br.com.douglas444.mltk.Point;

import java.util.List;
import java.util.Optional;

class STM extends Memory {

    STM() {}

    private STM(List<Point> sequence) {

        for (Point point : sequence) {
            super.predict(point);
            super.insert(point);
        }

    }

    private double calculateInterleavedTestTrainError() {

        return 1 - super.calculateWeight(super.size());

    }

    /** Returns the bisection containing the most recent samples.
     *
     * @return a list with the most recent samples.
     */
    private List<Point> getMostRecentBisection() {

        List<Point> set = super.getPoints();
        return super.getPoints().subList(set.size()/2, set.size());

    }

    Optional<Point> update(Point point) {

        super.insert(point);
        if (super.size() == Hyperparameter.L_MAX) {
            return Optional.of(super.getPoints().remove(0));
        }
        return Optional.empty();

    }

    /** Shrunk the memory size, sliding the dynamic window to the most recent
     * points such that the Interleaved Test-Train error is minimized.
     *
     * @return a list with the discarded points.
     */
    List<Point> shrunk() {

        STM minimum = this;
        STM bisection = new STM(this.getMostRecentBisection());

        while (bisection.size() >= Hyperparameter.L_MIN) {

            if (bisection.calculateInterleavedTestTrainError() <= minimum.calculateInterleavedTestTrainError()) {
                minimum = bisection;
            }

            bisection = new STM(bisection.getMostRecentBisection());

        }

        List<Point> discardedPoints = super.getPoints().subList(0, super.size() - minimum.size());

        if (minimum != this) {
            super.setPoints(minimum.getPoints());
            super.setPredictionLogs(minimum.getPredictionLogs());
        }


        return discardedPoints;

    }

}
