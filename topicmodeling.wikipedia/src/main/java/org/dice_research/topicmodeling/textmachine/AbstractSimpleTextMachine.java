package org.dice_research.topicmodeling.textmachine;

public abstract class AbstractSimpleTextMachine implements SimpleTextMachine {

	private SimpleTextMachineObserver observer;

	@Override
	public void registerObserver(SimpleTextMachineObserver observer) {
		this.observer = observer;
	}

	@Override
	public void analyze(String text) {
		this.reset();
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			processNextCharacter(chars[i], i);
		}
	}

	protected abstract void processNextCharacter(char c, int pos);
	
	protected abstract void reset();

	protected void foundPattern(int patternId, int startPos, int endPos) {
		observer.foundPattern(patternId, startPos, endPos);
	}
}
