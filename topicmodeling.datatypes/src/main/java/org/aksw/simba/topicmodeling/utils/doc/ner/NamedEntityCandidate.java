package org.aksw.simba.topicmodeling.utils.doc.ner;

import java.util.List;

public class NamedEntityCandidate {
    private int startPos;
    private int length;
    private List<String> candidates;

    public NamedEntityCandidate(int startPos, int length, List<String> candidates) {
        this.startPos = startPos;
        this.length = length;
        this.candidates = candidates;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return startPos + length;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }
}
