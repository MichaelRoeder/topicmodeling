package org.dice_research.topicmodeling.textmachine;

public interface SimpleTextMachineObserver {

	public void foundPattern(int patternId, int startPos, int endPos);
}
