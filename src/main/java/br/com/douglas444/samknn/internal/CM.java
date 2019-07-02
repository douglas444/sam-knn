package br.com.douglas444.samknn.internal;

import br.com.douglas444.mltk.Point;

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
    Optional<Double> predict(Point point) {
        super.getPoints().addAll(stm.getPoints());
        super.getPoints().addAll(ltm.getPoints());
        Optional<Double> label = super.predict(point);
        super.setPoints(new ArrayList<>());
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
