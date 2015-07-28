package org.aksw.simba.topicmodeling.utils.corpus.properties;

public class AbstractSimpleCorpusProperty<T> extends AbstractCorpusProperty {

    private static final long serialVersionUID = -8657635396026055335L;
    
    private T value;

    public AbstractSimpleCorpusProperty(T value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return get();
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
