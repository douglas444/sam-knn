package br.com.douglas444.samknn.core;

import br.com.douglas444.mltk.datastructure.DynamicConfusionMatrix;
import br.com.douglas444.mltk.datastructure.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SAMKNN {

    private int timestamp;
    private int losses;
    private final STM stm;
    private final LTM ltm;
    private final CM cm;
    private final DynamicConfusionMatrix confusionMatrix;

    public SAMKNN() {

        this.timestamp = 0;
        this.losses = 0;
        this.stm = new STM();
        this.ltm = new LTM();
        this.cm = new CM(this.stm, this.ltm);
        confusionMatrix = new DynamicConfusionMatrix(new ArrayList<>());
    }


    /** Predicts the label of a sample using the current model.
     *
     * @param sample the sample that the label will be predicted.
     * @return the predicted label  or empty if there is not
     * enough samples in the memory to execute the prediction.
     */
    public Optional<Integer> predict(final Sample sample) {

        final double wst = this.stm.calculateWeight(this.stm.size());
        final double wlt = this.ltm.calculateWeight(this.stm.size());
        final double wc = this.cm.calculateWeight(this.stm.size());

        final Optional<Integer> labelSTM = this.stm.predict(sample);
        final Optional<Integer> labelLTM = this.ltm.predict(sample);
        final Optional<Integer> labelCM = this.cm.predict(sample);

        if (wst >= Math.max(wlt, wc)) {
            return labelSTM;
        } else if (wlt >= wc) {
            return labelLTM;
        } else {
            return labelCM;
        }

    }

    /** Predicts the label of a sample using the current model and updates the
     * model using the true label.
     *
     * @param sample the sample that the label will be predicted and that the
     *              true label will be used to update the model.
     * @return the predicted label  or empty if there is not
     * enough samples in the memory to execute the prediction.
     */
    public Optional<Integer> predictAndUpdate(final Sample sample) {

        final Optional<Integer> label = this.predict(sample);
        if (!label.isPresent() || label.get() != sample.getY()) {
            ++this.losses;
        }
        ++this.timestamp;

        this.ltm.clean(this.stm, sample);
        final Optional<Sample> overflow = this.stm.update(sample);
        overflow.ifPresent(this.ltm::insert);
        final List<Sample> discardedSamples = this.stm.shrunk();

        if (!discardedSamples.isEmpty()) {

            final Memory memory = new Memory(discardedSamples);
            memory.clean(this.stm);
            this.ltm.insert(memory.getSamples());

        }

        while (this.stm.size() + this.ltm.size() > Hyperparameter.L_MAX) {
            this.ltm.compress();
        }

        this.confusionMatrix.addPrediction(sample.getY(), label.orElse(0), true);
        return label;

    }

    /** Calculates the accuracy of the current model.
     *
     * @return the accuracy of the current model.
     */
    public double calculatesAccuracy() {
        if (this.timestamp > 0) {
            return 1 - ((double) this.losses / this.timestamp);
        } else {
            return 0;
        }
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public int getLosses() {
        return this.losses;
    }

    public DynamicConfusionMatrix getConfusionMatrix() {
        return this.confusionMatrix;
    }
}
