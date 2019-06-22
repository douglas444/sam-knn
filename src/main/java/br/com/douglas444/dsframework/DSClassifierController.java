package br.com.douglas444.dsframework;

public interface DSClassifierController {

    /** Predicts the label of a point using the current model.
     *
     * @param point the point that the label will be predicted.
     * @return the predicted label.
     */
    double predictAndUpdate(Point point);

    /** Calculates the accuracy of the current model.
     *
     * @return the accuracy of the current model.
     */
    double getAccuracy();

}
