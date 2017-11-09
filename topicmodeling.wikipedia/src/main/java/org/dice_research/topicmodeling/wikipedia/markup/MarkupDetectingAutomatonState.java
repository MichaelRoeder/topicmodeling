package org.dice_research.topicmodeling.wikipedia.markup;

import com.carrotsearch.hppc.BitSet;

public interface MarkupDetectingAutomatonState {

    public BitSet getPatternBitSet();

    public void setPatternBitSet(BitSet patternBitSet);

    // public int getPosition();
    //
    // public void setPosition(int position);
}
