package org.aksw.simba.topicmodeling.utils.corpus.properties;

public class CorpusName extends AbstractSimpleCorpusProperty<String> implements ParseableCorpusProperty {

    private static final long serialVersionUID = 3123277554238612754L;

    public CorpusName(String name) {
        super(name);
    }

    @Override
    public void parseValue(String value) {
        set(value);
    }
}
