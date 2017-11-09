package org.dice_research.topicmodeling.textmachine;

public interface TextMachine {

    public void analyze(String text, TextMachineObserverState observerState);
    // public void analyze(String text, TextMachineObserverState observerState, BitSet patternBitSet);
}
