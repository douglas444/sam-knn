package br.com.douglas444.samknn;

import br.com.douglas444.datastream.DSClassifierController;
import br.com.douglas444.common.Point;
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
    public double getAccuracy() {
        return samknn.calculatesAccuracy();
    }

}
