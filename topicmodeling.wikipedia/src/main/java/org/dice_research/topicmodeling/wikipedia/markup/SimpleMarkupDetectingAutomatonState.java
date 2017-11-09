package org.dice_research.topicmodeling.wikipedia.markup;

import com.carrotsearch.hppc.BitSet;

public class SimpleMarkupDetectingAutomatonState implements MarkupDetectingAutomatonState {

    // public char chars[];
    // public int pos;
    // public int startPos;
    public BitSet patternBitSet;

    public SimpleMarkupDetectingAutomatonState() {
    }

    public SimpleMarkupDetectingAutomatonState(BitSet patternBitSet) {
        this.patternBitSet = patternBitSet;
    }

    @Override
    public BitSet getPatternBitSet() {
        return patternBitSet;
    }

    @Override
    public void setPatternBitSet(BitSet patternBitSet) {
        this.patternBitSet = patternBitSet;
    }
}
