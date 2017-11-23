package org.dice_research.topicmodeling.io;

public interface CorpusReaderDecorator extends CorpusReader {

    public CorpusReader getDecorated();
}
