package br.com.douglas444.samknn.core;

import br.com.douglas444.mltk.datastructure.Cluster;
import br.com.douglas444.mltk.datastructure.Sample;
import br.com.douglas444.mltk.clustering.kmeans.KMeans;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LTM extends Memory {

    /** Compresses the memory, clustering the samples and setting the centers
     * of the clusters as the new set of samples.
     */
    void compress() {

        final List<Sample> samples = new ArrayList<>();

        final HashMap<Integer, List<Sample>> samplesByLabel = new HashMap<>();
        super.getSamples().forEach(sample -> {
            samplesByLabel.putIfAbsent(sample.getY(), new ArrayList<>());
            samplesByLabel.get(sample.getY()).add(sample);
        });

        samplesByLabel.forEach((key, value) -> {
            List<Cluster> clusters = KMeans.execute(value, value.size() / 2, 0);
            clusters.forEach(cluster -> {
                samples.add(cluster.calculateCenter());
            });
        });

        super.setSamples(samples);
    }

}
