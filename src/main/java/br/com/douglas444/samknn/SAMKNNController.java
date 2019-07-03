package br.com.douglas444.samknn;

import br.com.douglas444.dsframework.DSClassifierController;
import br.com.douglas444.mltk.Point;
import br.com.douglas444.samknn.internal.SAMKNN;

import java.util.Optional;

public class SAMKNNController implements DSClassifierController {

    private SAMKNN samknn;

    public SAMKNNController() {
        this.samknn = new SAMKNN();
    }

    @Override
    public Optional<Double> predictAndUpdate(Point point) {
        return samknn.predictAndUpdate(point);
    }

    @Override
    public String getLog() {
        return " Timestamp: " + samknn.getTimestamp() + " Accuracy: " + samknn.calculatesAccuracy();
    }


}
