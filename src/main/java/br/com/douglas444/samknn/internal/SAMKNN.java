package br.com.douglas444.samknn.internal;

import br.com.douglas444.mltk.DynamicConfusionMatrix;
import br.com.douglas444.mltk.Point;

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


    /** Predicts the label of a point using the current model.
     *
     * @param point the point that the label will be predicted.
     * @return the predicted label  or empty if there is not
     * enough points in the memory to execute the prediction.
     */
    public Optional<Integer> predict(Point point) {

        double wst = this.stm.calculateWeight(this.stm.size());
        double wlt = this.ltm.calculateWeight(this.stm.size());
        double wc = this.cm.calculateWeight(this.stm.size());

        Optional<Integer> labelSTM = this.stm.predict(point);
        Optional<Integer> labelLTM = this.ltm.predict(point);
        Optional<Integer> labelCM = this.cm.predict(point);

        if (wst >= Math.max(wlt, wc)) {
            return labelSTM;
        } else if (wlt >= wc) {
            return labelLTM;
        } else {
            return labelCM;
        }

    }

    /** Predicts the label of a point using the current model and updates the
     * model using the true label.
     *
     * @param point the point that the label will be predicted and that the
     *              true label will be used to update the model.
     * @return the predicted label  or empty if there is not
     * enough points in the memory to execute the prediction.
     */
    public Optional<Integer> predictAndUpdate(Point point) {

        Optional<Integer> label = this.predict(point);
        if (!label.isPresent() || label.get() != point.getY()) {
            ++losses;
        }
        ++timestamp;

        ltm.clean(stm, point);
        Optional<Point> overflow = stm.update(point);
        overflow.ifPresent(value -> ltm.insert(value));
        List<Point> discardedPoints = stm.shrunk();

        if (!discardedPoints.isEmpty()) {

            Memory memory = new Memory(discardedPoints);
            memory.clean(stm);
            ltm.insert(memory.getPoints());

        }

        while (stm.size() + ltm.size() > Hyperparameter.L_MAX) {
            ltm.compress();
        }

        confusionMatrix.add(point.getY(), label.orElse(0), true);
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
