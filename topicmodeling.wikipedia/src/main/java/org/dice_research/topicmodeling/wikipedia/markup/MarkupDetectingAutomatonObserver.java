package org.dice_research.topicmodeling.wikipedia.markup;

public interface MarkupDetectingAutomatonObserver {

    public void foundPattern(int patternId, int startPos, int endPos,
            MarkupDetectingAutomatonObserverState observerState, MarkupDetectingAutomatonState automatonState);
}
