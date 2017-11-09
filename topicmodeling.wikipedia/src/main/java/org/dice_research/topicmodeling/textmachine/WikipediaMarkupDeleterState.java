package org.dice_research.topicmodeling.textmachine;

public class WikipediaMarkupDeleterState implements TextMachineObserverState {

    public WikipediaMarkupDeletingMachine observer;
    public String originalText;
    public StringBuilder cleanText;
    public int pos;

    public WikipediaMarkupDeleterState(WikipediaMarkupDeletingMachine observer, String originalText,
            StringBuilder cleanText, int pos) {
        this.observer = observer;
        this.originalText = originalText;
        this.cleanText = cleanText;
        this.pos = pos;
    }

    @Override
    public TextMachineObserver getObserver() {
        return observer;
    }
}
