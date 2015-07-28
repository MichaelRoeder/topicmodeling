package org.aksw.simba.topicmodeling.utils.vocabulary;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

public class AbstractVocabularyDecorator implements VocabularyDecorator, Externalizable {

    private static final long serialVersionUID = -8157697283388720665L;

    private Vocabulary decoratedVocabulary;

    public AbstractVocabularyDecorator(Vocabulary decoratedVocabulary) {
        this.decoratedVocabulary = decoratedVocabulary;
    }

    protected AbstractVocabularyDecorator() {
    }

    @Override
    public int size() {
        return decoratedVocabulary.size();
    }

    @Override
    public void add(String word) {
        decoratedVocabulary.add(word);
    }

    @Override
    public Integer getId(String word) {
        return decoratedVocabulary.getId(word);
    }

    @Override
    public Iterator<String> iterator() {
        return decoratedVocabulary.iterator();
    }

    @Override
    public String getWord(int wordId) {
        return decoratedVocabulary.getWord(wordId);
    }

    @Override
    public void setWord(String word, String newWord) {
        decoratedVocabulary.setWord(word, newWord);
    }

    @Override
    public Vocabulary getDecoratedVocabulary() {
        return decoratedVocabulary;
    }

    public void setDecoratedVocabulary(Vocabulary decoratedVocabulary) {
        this.decoratedVocabulary = decoratedVocabulary;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        decoratedVocabulary = (Vocabulary) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(decoratedVocabulary);
    }
}
