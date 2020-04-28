package br.com.douglas444.samknn.core;

import br.com.douglas444.mltk.datastructure.Sample;

import java.util.List;
import java.util.Optional;

class STM extends Memory {

    STM() {}

    private STM(List<Sample> sequence) {

        for (Sample sample : sequence) {
            super.predict(sample);
            super.insert(sample);
        }

    }

    private double calculateInterleavedTestTrainError() {

        return 1 - super.calculateWeight(super.size());

    }

    /** Returns the bisection containing the most recent samples.
     *
     * @return a list with the most recent samples.
     */
    private List<Sample> getMostRecentBisection() {

        final List<Sample> set = super.getSamples();
        return super.getSamples().subList(set.size()/2, set.size());

    }

    Optional<Sample> update(final Sample sample) {

        super.insert(sample);
        if (super.size() == Hyperparameter.L_MAX) {
            return Optional.of(super.getSamples().remove(0));
        }
        return Optional.empty();

    }

    /** Shrunk the memory size, sliding the dynamic window to the most recent
     * samples such that the Interleaved Test-Train error is minimized.
     *
     * @return a list with the discarded samples.
     */
    List<Sample> shrunk() {

        STM minimum = this;
        STM bisection = new STM(this.getMostRecentBisection());

        while (bisection.size() >= Hyperparameter.L_MIN) {

            if (bisection.calculateInterleavedTestTrainError() <= minimum.calculateInterleavedTestTrainError()) {
                minimum = bisection;
            }

            bisection = new STM(bisection.getMostRecentBisection());

        }

        final List<Sample> discardedSamples = super.getSamples().subList(0, super.size() - minimum.size());

        if (minimum != this) {
            super.setSamples(minimum.getSamples());
            super.setPredictionLogs(minimum.getPredictionLogs());
        }


        return discardedSamples;

    }

}
