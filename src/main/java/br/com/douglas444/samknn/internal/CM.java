package br.com.douglas444.samknn.internal;

import br.com.douglas444.mltk.Sample;

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
    Optional<Integer> predict(Sample sample) {
        super.getSamples().addAll(stm.getSamples());
        super.getSamples().addAll(ltm.getSamples());
        Optional<Integer> label = super.predict(sample);
        super.setSamples(new ArrayList<>());
        return label;
    }

    @Override
    public List<Sample> getSamples() {
        List<Sample> samples = new ArrayList<>();
        samples.addAll(stm.getSamples());
        samples.addAll(ltm.getSamples());
        return samples;
    }

}
