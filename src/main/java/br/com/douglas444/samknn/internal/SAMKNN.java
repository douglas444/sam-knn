package br.com.douglas444.samknn.internal;

import br.com.douglas444.dsframework.Point;

import java.util.List;
import java.util.Optional;

public class SAMKNN {

    private int timestamp;
    private int losses;
    private STM stm;
    private LTM ltm;
    private CM cm;

    public SAMKNN() {

        this.timestamp = 0;
        this.losses = 0;
        this.stm = new STM();
        this.ltm = new LTM();
        this.cm = new CM(this.stm, this.ltm);
    }


    /** Predicts the label of a point using the current model.
     *
     * @param point the point that the label will be predicted.
     * @return the predicted label  or empty if there is not
     * enough points in the memory to execute the prediction.
     */
    public Optional<Double> predict(Point point) {

        double wst = this.stm.calculateWeight(this.stm.getPoints());
        double wlt = this.ltm.calculateWeight(this.stm.getPoints());
        double wc = this.cm.calculateWeight(this.stm.getPoints());

        Optional<Double> label;
        if (wst > Math.max(wlt, wc)) {
            label = this.stm.predict(point);
        } else if (wlt > Math.max(wst, wc)) {
            label = this.ltm.predict(point);
        } else  {
            label = this.cm.predict(point);
        }

        return label;

    }

    /** Predicts the label of a point using the current model and updates the
     * model using the true label.
     *
     * @param point the point that the label will be predicted and that the
     *              true label will be used to update the model.
     * @return the predicted label  or empty if there is not
     * enough points in the memory to execute the prediction.
     */
    public Optional<Double> predictAndUpdate(Point point) {

        Optional<Double> label = this.predict(point);
        if (!label.isPresent() || label.get() != point.getY()) {
            ++losses;
        }
        ++timestamp;

        List<Point> discardedPoints = null;

        if (stm.isFull()) {
            discardedPoints = stm.shrunk();
        }

        stm.insert(point);
        ltm.clean(stm, point);

        if (discardedPoints != null) {

            Memory memory = new Memory(discardedPoints);
            memory.clean(stm);

            if (ltm.getRemainingSpace() < memory.getPoints().size()) {
                ltm.compress();
            }

            ltm.insert(memory.getPoints());
        }

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
}
