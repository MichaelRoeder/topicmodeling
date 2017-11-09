package org.dice_research.topicmodeling.textmachine;

public interface SimpleTextMachine {

    public void registerObserver(SimpleTextMachineObserver observer);

    public void analyze(String text);

}
