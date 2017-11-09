package org.dice_research.topicmodeling.textmachine;

import com.carrotsearch.hppc.BitSet;

public class WikipediaMarkupDetectingMachineState implements TextMachineState {

    private BitSet patternBitSet;

    public WikipediaMarkupDetectingMachineState(BitSet patternBitSet) {
        this.patternBitSet = patternBitSet;
    }

    @Override
    public BitSet getPatternBitSet() {
        return patternBitSet;
    }
}
