package br.com.douglas444.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DynamicConfusionMatrix {

    private int knownPredictedLabelsCount;
    private int novelPredictedLabelsCount;
    private int actualLabelsCount;
    private int predictedLabelsCount;

    private HashMap<Integer, Integer> actualLabelEnumByActualLabel;
    private HashMap<Integer, Integer> predictedLabelEnumByPredictedLabel;

    private HashMap<Integer, List<Integer>> predictionsAsKnownByActualLabel;
    private HashMap<Integer, List<Integer>> predictionsAsNovelByActualLabel;

    public DynamicConfusionMatrix(List<Integer> knownLabels) {

        actualLabelEnumByActualLabel = new HashMap<>();
        predictedLabelEnumByPredictedLabel = new HashMap<>();

        this.knownPredictedLabelsCount = knownLabels.size();
        this.novelPredictedLabelsCount = 0;

        this.predictionsAsKnownByActualLabel = new HashMap<>();
        this.predictionsAsNovelByActualLabel = new HashMap<>();

        knownLabels.forEach(label -> {

            actualLabelEnumByActualLabel.put(label, actualLabelsCount);
            predictedLabelEnumByPredictedLabel.put(label, actualLabelsCount);
            ++actualLabelsCount;

            this.predictionsAsKnownByActualLabel.put(label,
                    new ArrayList<>(Collections.nCopies(knownLabels.size(), 0)));

            this.predictionsAsNovelByActualLabel.put(label, new ArrayList<>());

        });

    }

    public void add(int actualLabel, int predictedLabel, boolean isNovel) {


        if (actualLabelEnumByActualLabel.get(actualLabel) == null) {

            actualLabelEnumByActualLabel.put(actualLabelsCount, actualLabel);
            predictionsAsKnownByActualLabel.put(actualLabelsCount, Collections.nCopies(knownPredictedLabelsCount, 0));
            predictionsAsNovelByActualLabel.put(actualLabelsCount, Collections.nCopies(novelPredictedLabelsCount, 0));
            ++actualLabelsCount;
        }

        if (predictedLabelEnumByPredictedLabel.get(predictedLabel) == null) {

            predictedLabelEnumByPredictedLabel.put(predictedLabelsCount, predictedLabel);
            predictionsAsNovelByActualLabel.forEach((key, value) -> value.add(0));
            ++predictedLabelsCount;
        }

        int actualLabelEnum = actualLabelEnumByActualLabel.get(actualLabel);
        int predictedLabelEnum = predictedLabelEnumByPredictedLabel.get(predictedLabel);

        if (isNovel) {

            int count = predictionsAsNovelByActualLabel.get(actualLabelEnum).get(predictedLabelEnum);
            predictionsAsNovelByActualLabel.get(actualLabelEnum).add(predictedLabelEnum, count + 1);

        } else {

            int count = predictionsAsKnownByActualLabel.get(actualLabelEnum).get(predictedLabelEnum);
            predictionsAsKnownByActualLabel.get(actualLabelEnum).add(predictedLabelEnum, count + 1);

        }

    }


}
