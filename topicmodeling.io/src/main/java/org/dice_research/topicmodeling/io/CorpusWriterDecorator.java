package org.dice_research.topicmodeling.io;

public interface CorpusWriterDecorator extends CorpusWriter {

    public CorpusWriter getDecorated();
}
