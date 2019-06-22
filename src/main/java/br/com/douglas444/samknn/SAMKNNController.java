package br.com.douglas444.samknn;

import br.com.douglas444.dsframework.DSClassifierController;
import br.com.douglas444.dsframework.Point;
import br.com.douglas444.samknn.internal.SAMKNN;

public class SAMKNNController implements DSClassifierController {

    private SAMKNN samknn;

    public SAMKNNController() {
        this.samknn = new SAMKNN();
    }

    /** Predict label of a point using the current model.
     *
     * @param point the point that the label will be predicted.
     * @return the predicted label.
     */
    @Override
    public double predictAndUpdate(Point point) {
        return samknn.predictAndUpdate(point);
    }

    /** Calculates the accuracy of the current model.
     *
     * @return the accuracy of the current model.
     */
    @Override
    public double getAccuracy() {
        return (double) samknn.getLosses() / samknn.getTimestamp();
    }

}
