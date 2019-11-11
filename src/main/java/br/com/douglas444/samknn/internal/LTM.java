package br.com.douglas444.samknn.internal;

import br.com.douglas444.mltk.Cluster;
import br.com.douglas444.mltk.Sample;
import br.com.douglas444.mltk.kmeans.KMeansPlusPlus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LTM extends Memory {

    /** Compresses the memory, clustering the samples and setting the centers
     * of the clusters as the new set of samples.
     */
    void compress() {

        List<Sample> samples = new ArrayList<>();

        HashMap<Integer, List<Sample>> samplesByLabel = new HashMap<>();
        super.getSamples().forEach(sample -> {
            samplesByLabel.putIfAbsent(sample.getY(), new ArrayList<>());
            samplesByLabel.get(sample.getY()).add(sample);
        });

        samplesByLabel.forEach((key, value) -> {
            KMeansPlusPlus kMeansPlusPlus = new KMeansPlusPlus(value, value.size() / 2);
            List<Cluster> clusters = kMeansPlusPlus.fit();
            clusters.forEach(cluster -> {
                samples.add(cluster.calculateCenter());
            });
        });

        super.setSamples(samples);
    }

}
