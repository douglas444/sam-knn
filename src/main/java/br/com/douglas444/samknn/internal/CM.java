package br.com.douglas444.samknn.internal;

import br.com.douglas444.dsframework.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CM extends Memory {

    private STM stm;
    private LTM ltm;

    CM (STM stm, LTM ltm) {
        this.stm = stm;
        this.ltm = ltm;
    }

    @Override
    double calculateWeight(List<Point> points) {
        super.getPoints().addAll(stm.getPoints());
        super.getPoints().addAll(ltm.getPoints());
        double weight = super.calculateWeight(points);
        super.getPoints().clear();
        return weight;
    }

    @Override
    Optional<Double> predict(Point point) {
        super.getPoints().addAll(stm.getPoints());
        super.getPoints().addAll(ltm.getPoints());
        Optional<Double> label = super.predict(point);
        super.getPoints().clear();
        return label;
    }

    @Override
    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();
        points.addAll(stm.getPoints());
        points.addAll(ltm.getPoints());
        return points;
    }

}
