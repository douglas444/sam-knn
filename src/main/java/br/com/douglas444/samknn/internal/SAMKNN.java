package br.com.douglas444.samknn.internal;

import br.com.douglas444.dsframework.Point;

import java.util.List;

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
     * @return the predicted label.
     */
    public double predict(Point point) {

        double wst = this.stm.calculateWeight(this.stm.getPoints());
        double wlt = this.ltm.calculateWeight(this.stm.getPoints());
        double wc = this.cm.calculateWeight(this.stm.getPoints());

        double y;
        if (wst > Math.max(wlt, wc)) {
            y = this.stm.predict(point);
        } else if (wlt > Math.max(wst, wc)) {
            y = this.ltm.predict(point);
        } else  {
            y = this.cm.predict(point);
        }

        return y;

    }

    /** Predicts the label of a point using the current model and updates the
     * model using the true label.
     *
     * @param point the point that the label will be predicted and that the
     *              true label will be used to update the model.
     * @return the predicted label.
     */
    public double predictAndUpdate(Point point) {

        double y = this.predict(point);
        if (y != point.getY()) {
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

        return y;

    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getLosses() {
        return losses;
    }
}
