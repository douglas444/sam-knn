package br.com.douglas444.samknn.internal.kmeans;

import br.com.douglas444.dsframework.Point;
import java.util.*;

public class KMeansPlusPlus {

    private List<Point> points;
    private List<Cluster> clusters;

    public KMeansPlusPlus(List<Point> points, int k) {

        this.points = points;

        Set<Point> centers = chooseCenters(points, k);
        this.clusters = groupByClosestCenter(points, centers);

    }

    /** Chooses the initials centers.
     *
     * @param points points set.
     * @param k number of centers.
     * @return a list of centers.
     */
    private static Set<Point> chooseCenters(List<Point> points, int k) {

        Set<Point> centers = new HashSet<>();

        for (int i = 0; i < k; ++i) {
            Point center = randomSelectNextCenter(points, centers);
            //points.remove(center);
            centers.add(center);
        }

        return centers;

    }

    /** Selects the next center in a set of points.
     *
     * @param points list with the candidates points.
     * @param centers the list containing the current centers.
     * @return the next center.
     */
    private static Point randomSelectNextCenter(List<Point> points, Set<Point> centers) {

        Random generator = new Random();

        double roulette = 1;
        HashMap<Point, Double> probabilityByPoint = mapProbabilityByPoint(points, centers);

        List<Map.Entry<Point, Double>> entries = new ArrayList<>(probabilityByPoint.entrySet());
        Iterator<Map.Entry<Point, Double>> iterator = entries.iterator();
        Point selectedCenter = iterator.next().getKey();

        while (iterator.hasNext()) {

            Map.Entry<Point, Double> entry = iterator.next();
            double r = generator.nextDouble() * roulette;
            if (r <= entry.getValue()) {
                selectedCenter = entry.getKey();
            } else {
                roulette -= entry.getValue();
            }

        }

        return selectedCenter;

    }

    /** Calculates the probability of each point of be select as the next center.
     *
     * @param points list with the candidates points.
     * @param centers the list containing the current centers.
     * @return a map of probability by point.
     */
    private static HashMap<Point, Double> mapProbabilityByPoint(List<Point> points, Set<Point> centers) {

        HashMap<Point, Double> probabilityByPoint = new HashMap<>();
        points.forEach(point -> {
            probabilityByPoint.put(point, distanceToTheClosestCenter(point, centers));
        });

        double sum = probabilityByPoint.values().stream().mapToDouble(Double::doubleValue).sum();
        probabilityByPoint.values().forEach(probability -> probability /= sum);

        return probabilityByPoint;

    }

    /** Calculates de distance of a point to the closest center.
     *
     * @param point the target point.
     * @param centers the list containing the centers.
     * @return the distance of the point to the closest center.
     */
    private static double distanceToTheClosestCenter(Point point, Set<Point> centers) {

        Point closestCenter = getClosestCenter(point, centers);

        if (closestCenter != null) {
            return point.distance(closestCenter);
        } else {
            return 0;
        }

    }

    /** Calculates the closest center.
     *
     * @param point the target point.
     * @param centers the list containing the centers.
     * @return the closest center.
     */
    private static Point getClosestCenter(Point point, Set<Point> centers) {

        return centers
                .stream()
                .min(Comparator.comparing(center -> center.distance(point)))
                .orElse(null);

    }

    /** Generates a list of clusters, grouping a list of point by the closest center.
     *
     * @param points points to be grouped.
     * @param centers the list containing the centers.
     * @return a list of clusters.
     */
    private static List<Cluster> groupByClosestCenter(List<Point> points, Set<Point> centers) {

        HashMap<Point, List<Point>> pointsByCenter = new HashMap<>();

        centers.forEach(center -> {
            pointsByCenter.put(center, new ArrayList<>());
        });

        points.forEach(point -> {
            Point closestCenter = getClosestCenter(point, centers);
            if (closestCenter != null) {
                pointsByCenter.get(closestCenter).add(point);
            }
        });

        List<Cluster> clusters = new ArrayList<>();
        pointsByCenter.forEach((key, value) -> {
            if (value.size() > 0) {
                clusters.add(new Cluster(value));
            }
        });
        return clusters;

    }

    /** Calculates de center of the current clusters and regroup the points
     * using the new centers. Repeat the process until the centers dont change.
     *
     * @return a list of clusters.
     */
    public List<Cluster> fit() {

        Set<Point> newCenters = new HashSet<>();
        Set<Point> oldCenters;

        do {

            oldCenters = new HashSet<>(newCenters);
            newCenters = new HashSet<>();

            for (Cluster cluster : clusters) {
                newCenters.add(cluster.calculateCenter());
            }

            clusters = groupByClosestCenter(points, newCenters);

        } while(!oldCenters.containsAll(newCenters));

        return clusters;
    }

}
