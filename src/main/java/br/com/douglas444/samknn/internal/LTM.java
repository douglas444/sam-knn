package br.com.douglas444.samknn.internal;

import br.com.douglas444.common.Cluster;
import br.com.douglas444.kmeans.KMeansPlusPlus;
import br.com.douglas444.common.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LTM extends Memory {

    /** Compresses the memory, clustering the points and setting the centers
     * of the clusters as the new set of points.
     */
    void compress() {

        List<Point> points = new ArrayList<>();

        HashMap<Double, List<Point>> pointsByLabel = new HashMap<>();
        super.getPoints().forEach(point -> {
            pointsByLabel.putIfAbsent(point.getY(), new ArrayList<>());
            pointsByLabel.get(point.getY()).add(point);
        });

        pointsByLabel.forEach((key, value) -> {
            KMeansPlusPlus kMeansPlusPlus = new KMeansPlusPlus(value, value.size() / 2);
            List<Cluster> clusters = kMeansPlusPlus.fit();
            clusters.forEach(cluster -> {
                points.add(cluster.calculateCenter());
            });
        });

        super.setPoints(points);
    }

}
