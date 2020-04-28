package br.com.douglas444.samknn.core;

import br.com.douglas444.mltk.datastructure.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CM extends Memory {

    private final STM stm;
    private final LTM ltm;

    CM (STM stm, LTM ltm) {
        this.stm = stm;
        this.ltm = ltm;
    }

    @Override
    Optional<Integer> predict(final Sample sample) {
        super.getSamples().addAll(stm.getSamples());
        super.getSamples().addAll(ltm.getSamples());
        final Optional<Integer> label = super.predict(sample);
        super.setSamples(new ArrayList<>());
        return label;
    }

    @Override
    public List<Sample> getSamples() {
        final List<Sample> samples = new ArrayList<>();
        samples.addAll(stm.getSamples());
        samples.addAll(ltm.getSamples());
        return samples;
    }

}
