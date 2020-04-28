package br.com.douglas444.samknn;

import br.com.douglas444.dsframework.DSClassifierController;
import br.com.douglas444.mltk.datastructure.Sample;
import br.com.douglas444.samknn.core.SAMKNN;

import java.util.Optional;

public class SAMKNNController implements DSClassifierController {

    private SAMKNN samknn;

    public SAMKNNController() {
        this.samknn = new SAMKNN();
    }

    @Override
    public Optional<Integer> predictAndUpdate(Sample sample) {
        return samknn.predictAndUpdate(sample);
    }

    @Override
    public String getLog() {

        return " Timestamp: " + samknn.getTimestamp() +
                " Accuracy: " + samknn.calculatesAccuracy() +
                "\n\n" +
                samknn.getConfusionMatrix().toString();
    }


}
