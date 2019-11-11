package br.com.douglas444.samknn.internal;

import br.com.douglas444.mltk.DistanceComparator;
import br.com.douglas444.mltk.Sample;

import java.util.*;
import java.util.stream.Collectors;

class Memory {

    private List<Sample> samples;
    private List<Boolean> predictionLogs;

    Memory() {
        this.samples =  new ArrayList<>();
        this.predictionLogs = new ArrayList<>();
    }

    Memory(List<Sample> samples) {
        this.samples =  samples;
        this.predictionLogs = new ArrayList<>();
    }

    /** Gets the k nearest samples.
     *
     * @param sample the sample from where the search will be executed.
     * @param k number of samples.
     * @return a list with the the k nearest samples.
     */
    private List<Sample> getKNearestNeighbors(Sample sample, int k) {

        List<Sample> sampleList = new ArrayList<>(this.samples);
        sampleList.remove(sample);
        sampleList.sort(new DistanceComparator(sample));

        if (k > sampleList.size()) {
            k = sampleList.size();
        }

        return sampleList.subList(0, k);
    }

    /** Cleans the samples that are inconsistent to another memory in regard of a sample.
     *
     * @param memory the memory with which the samples are inconsistent.
     * @param sample the sample that will serve as base of the cleaning process.
     */
    void clean(Memory memory, Sample sample) {

        List<Sample> nearestNeighbors = memory
                .getKNearestNeighbors(sample, Hyperparameter.K)
                .stream()
                .filter(neighbor -> neighbor.getY() == sample.getY())
                .collect(Collectors.toList());

        if (nearestNeighbors.size() > 0) {
            double threshold = sample.distance(nearestNeighbors.get(nearestNeighbors.size() - 1));

            List<Sample> toBeRemoved = this
                    .getKNearestNeighbors(sample, Hyperparameter.K)
                    .stream()
                    .filter(neighbor -> (neighbor.distance(sample) <= threshold) &&
                            (neighbor.getY() != sample.getY()))
                    .collect(Collectors.toList());

            this.samples.removeAll(toBeRemoved);
        }

    }

    /** Cleans the samples that are inconsistent to another memory.
     *
     * @param memory the memory with which the samples are inconsistent.
     */
    void clean(Memory memory) {

        memory.getSamples().forEach(sample -> this.clean(memory, sample));

    }

    double calculateWeight(int m) {

        return this.predictionLogs
                .subList(this.predictionLogs.size() - m, this.predictionLogs.size())
                .stream()
                .mapToDouble(predictionLog -> {
                    if (predictionLog) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .sum() / m;
    }

    /** Predicts the label of a sample.
     *
     * @param sample the sample which the label will be predicted.
     * @return the predicted label of the sample or empty if there is not
     * enough samples in the memory to execute the prediction.
     */
    Optional<Integer> predict(Sample sample) {

        HashMap<Integer, Double> inverseDistanceSumPerY = new HashMap<>();

        this.getKNearestNeighbors(sample, Hyperparameter.K).forEach(neighbor -> {
            inverseDistanceSumPerY.putIfAbsent(neighbor.getY(), 0.0);
            double sum = inverseDistanceSumPerY.get(neighbor.getY());
            inverseDistanceSumPerY.put(neighbor.getY(), sum + (double) 1 / sample.distance(neighbor));

        });

        Map.Entry<Integer, Double> maxEntry = null;
        for (Map.Entry<Integer, Double> entry : inverseDistanceSumPerY.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        Optional<Integer> label;
        if (maxEntry == null) {
            label = Optional.empty();
        } else {
            label = Optional.of(maxEntry.getKey());
        }

        if (label.isPresent() && label.get() == sample.getY()) {
            predictionLogs.add(true);
        } else {
            predictionLogs.add(false);
        }
        if (predictionLogs.size() == Hyperparameter.L_MAX) {
            predictionLogs.remove(0);
        }

        return label;
    }

    /** Updates the model inserting a sample into the memory.
     *
     * @param sample the sample to be inserted
     */
    void insert(Sample sample) {
        samples.add(sample);
    }

    /** Updates the model inserting a list of samples into the memory.
     *
     * @param samples the list of samples to be inserted
     */
    void insert(List<Sample> samples) {
        this.samples.addAll(samples);
    }

    /** Returns the number of samples in the memory.
     *
     * @return remaining space in the memory.
     */
    int size() {
        return this.getSamples().size();
    }

    List<Sample> getSamples() {
        return samples;
    }

    void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    List<Boolean> getPredictionLogs() {
        return predictionLogs;
    }

    void setPredictionLogs(List<Boolean> predictionLogs) {
        this.predictionLogs = predictionLogs;
    }
}
