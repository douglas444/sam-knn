package br.com.douglas444.datastream;
import br.com.douglas444.common.Point;

import java.util.Optional;

public interface DSClassifierController {

    /** Tries to predicts the label of a point using the current model and
     * then update the model using the true label.
     *
     * @param point the point that the label will be predicted.
     * @return the predicted label or empty if the label could not be predicted.
     */
    Optional<Double> predictAndUpdate(Point point);

    /** Returns the accuracy of the current model.
     *
     * @return the accuracy of the current model.
     */
    double getAccuracy();

}
