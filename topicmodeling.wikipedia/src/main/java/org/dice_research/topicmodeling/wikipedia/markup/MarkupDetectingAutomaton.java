package org.dice_research.topicmodeling.wikipedia.markup;

import com.carrotsearch.hppc.BitSet;

public interface MarkupDetectingAutomaton {

    public void analyze(String text, MarkupDetectingAutomatonObserverState observerState, BitSet patternBitSet);
}
