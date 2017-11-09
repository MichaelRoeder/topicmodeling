package org.dice_research.topicmodeling.textmachine;

public interface TextMachineObserver {

    public void foundPattern(int patternId, int startPos,
            int endPos, TextMachineObserverState observerState);
    // public void foundPattern(int patternId, int startPos,
    // int endPos, TextMachineObserverState observerState, TextMachineState machineState);
}
