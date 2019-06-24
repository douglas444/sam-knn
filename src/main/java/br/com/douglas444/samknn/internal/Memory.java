package br.com.douglas444.samknn.internal;

import br.com.douglas444.dsframework.DistanceComparator;
import br.com.douglas444.dsframework.Point;

import java.util.*;
import java.util.stream.Collectors;

class Memory {

    private List<Point> points;

    Memory() {
        this.points =  new ArrayList<>();
    }

    Memory(List<Point> points) {
        this.points =  points;
    }

    /** Gets the k nearest points.
     *
     * @param point the point from where the search will be executed.
     * @param k number of points.
     * @return a list with the the k nearest points.
     */
    private List<Point> getKNearestNeighbors(Point point, int k) {



        List<Point> pointList = new ArrayList<>(this.points);
        pointList.remove(point);
        pointList.sort(new DistanceComparator(point));

        if (k > pointList.size()) {
            k = pointList.size();
        }

        return pointList.subList(0, k);
    }

    /** Cleans the points that are inconsistent to another memory in regard of a point.
     *
     * @param memory the memory with which the points are inconsistent.
     * @param point the point that will serve as base of the cleaning process.
     */
    void clean(Memory memory, Point point) {

        if (this.size() < 1) {
            return;
        }

        List<Point> nearestNeighbors = memory
                .getKNearestNeighbors(point, Hyperparameter.K)
                .stream()
                .filter(neighbor -> neighbor.getY() == point.getY())
                .collect(Collectors.toList());

        if (nearestNeighbors.size() > 0) {
            double threshold = point.distance(nearestNeighbors.get(nearestNeighbors.size() - 1));

            List<Point> toBeRemoved = this
                    .getKNearestNeighbors(point, Hyperparameter.K)
                    .stream()
                    .filter(neighbor -> (neighbor.distance(point) <= threshold) &&
                            (neighbor.getY() != point.getY()))
                    .collect(Collectors.toList());

            this.points.removeAll(toBeRemoved);
        }

    }

    /** Cleans the points that are inconsistent to another memory.
     *
     * @param memory the memory with which the points are inconsistent.
     */
    void clean(Memory memory) {

        if (this.size() < 1) {
            return;
        }

        memory.getPoints().forEach(point -> this.clean(memory, point));

    }

    /** Calculates memory weight given a list of points to be predicted.
     *
     * @param points the list of points to be predicted.
     * @return the weight calculated.
     */
    double calculateWeight(List<Point> points) {

        return points
                .stream()
                .mapToDouble(point -> {
                    Optional<Double> label = predict(point);
                    if (label.isPresent() && label.get() == point.getY()) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .sum() / points.size();
    }

    /** Predicts the label of a point.
     *
     * @param point the point which the label will be predicted.
     * @return the predicted label of the point or empty if there is not
     * enough points in the memory to execute the prediction.
     */
    Optional<Double> predict(Point point) {

        HashMap<Double, Double> inverseDistanceSumPerY = new HashMap<>();

        this.getKNearestNeighbors(point, Hyperparameter.K).forEach(neighbor -> {
            inverseDistanceSumPerY.putIfAbsent(neighbor.getY(), 0.0);
            double sum = inverseDistanceSumPerY.get(neighbor.getY());
            inverseDistanceSumPerY.put(neighbor.getY(), sum + (double) 1 / point.distance(neighbor));

        });

        Map.Entry<Double, Double> maxEntry = null;
        for (Map.Entry<Double, Double> entry : inverseDistanceSumPerY.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        if (maxEntry == null) {
            return Optional.empty();
        } else {
            return Optional.of(maxEntry.getKey());
        }
    }

    /** Checks if the memory has reached the maximum size.
     *
     * @return true if the memory has reached the maximum size, false
     * otherwise.
     */
    boolean isFull() {
        return points.size() >= Hyperparameter.L_MAX;
    }

    /** Updates the model inserting a point into the memory.
     *
     * @param point the point to be inserted
     */
    void insert(Point point) {
        points.add(point);
    }

    /** Updates the model inserting a list of points into the memory.
     *
     * @param points the list of points to be inserted
     */
    void insert(List<Point> points) {
        this.points.addAll(points);
    }

    /** Calculates the remaining space in the memory.
     *
     * @return remaining space in the memory.
     */
    int getRemainingSpace() {
        return  Hyperparameter.L_MAX - this.size();
    }

    /** Returns the number of points in the memory.
     *
     * @return remaining space in the memory.
     */
    int size() {
        return this.getPoints().size();
    }

    List<Point> getPoints() {
        return points;
    }

    void setPoints(List<Point> points) {
        this.points = points;
    }
}
