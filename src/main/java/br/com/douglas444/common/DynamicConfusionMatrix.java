package br.com.douglas444.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DynamicConfusionMatrix {

    private int knownPredictedLabelsCount;
    private int novelPredictedLabelsCount;
    private List<Double> actualLabels;
    private HashMap<Double, List<Integer>> predictionsAsKnownPerActualLabel;
    private HashMap<Double, List<Integer>> predictionsAsNovelPerActualLabel;

    public DynamicConfusionMatrix(List<Double> knownLabels) {

        this.knownPredictedLabelsCount = knownLabels.size();
        this.novelPredictedLabelsCount = 0;

        this.actualLabels = new ArrayList<>(knownLabels);
        this.predictionsAsKnownPerActualLabel = new HashMap<>();
        this.predictionsAsNovelPerActualLabel = new HashMap<>();

        knownLabels.forEach(label -> {

            this.predictionsAsKnownPerActualLabel.put(label,
                    new ArrayList<>(Collections.nCopies(knownLabels.size(), 0)));

            this.predictionsAsNovelPerActualLabel.put(label, new ArrayList<>());

        });

    }

    public void add(double actualLabel, double predictedLabel, boolean isNovel) {

        if(!actualLabels.contains(actualLabel)) {
            predictionsAsKnownPerActualLabel.put(actualLabel, Collections.nCopies(knownPredictedLabelsCount, 0));
            predictionsAsNovelPerActualLabel.put(actualLabel, Collections.nCopies(novelPredictedLabelsCount, 0));
            actualLabels.add(actualLabel);
        }

        if (isNovel) {

            if (!predictionsAsNovelPerActualLabel.keySet().contains(predictedLabel)) {
                predictionsAsNovelPerActualLabel.forEach((key, value) -> value.add(0));
            }


        }

    }


}
