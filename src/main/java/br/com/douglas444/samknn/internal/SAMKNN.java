package br.com.douglas444.samknn.internal;

import br.com.douglas444.mltk.DynamicConfusionMatrix;
import br.com.douglas444.mltk.Sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SAMKNN {

    private int timestamp;
    private int losses;
    private STM stm;
    private LTM ltm;
    private CM cm;
    private DynamicConfusionMatrix confusionMatrix;

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
    public Optional<Integer> predict(Sample sample) {

        double wst = this.stm.calculateWeight(this.stm.size());
        double wlt = this.ltm.calculateWeight(this.stm.size());
        double wc = this.cm.calculateWeight(this.stm.size());

        Optional<Integer> labelSTM = this.stm.predict(sample);
        Optional<Integer> labelLTM = this.ltm.predict(sample);
        Optional<Integer> labelCM = this.cm.predict(sample);

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
    public Optional<Integer> predictAndUpdate(Sample sample) {

        Optional<Integer> label = this.predict(sample);
        if (!label.isPresent() || label.get() != sample.getY()) {
            ++losses;
        }
        ++timestamp;

        ltm.clean(stm, sample);
        Optional<Sample> overflow = stm.update(sample);
        overflow.ifPresent(value -> ltm.insert(value));
        List<Sample> discardedSamples = stm.shrunk();

        if (!discardedSamples.isEmpty()) {

            Memory memory = new Memory(discardedSamples);
            memory.clean(stm);
            ltm.insert(memory.getSamples());

        }

        while (stm.size() + ltm.size() > Hyperparameter.L_MAX) {
            ltm.compress();
        }

        confusionMatrix.add(sample.getY(), label.orElse(0), true);
        return label;

    }

    /** Calculates the accuracy of the current model.
     *
     * @return the accuracy of the current model.
     */
    public double calculatesAccuracy() {
        if (timestamp > 0) {
            return 1 - ((double) losses / timestamp);
        } else {
            return 0;
        }
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getLosses() {
        return losses;
    }

    public DynamicConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
    }

    public void setConfusionMatrix(DynamicConfusionMatrix confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }
}
