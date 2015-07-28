package org.aksw.simba.topicmodeling.utils.corpus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.aksw.simba.topicmodeling.utils.corpus.properties.CorpusProperty;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public abstract class AbstractCorpusDecorator implements CorpusDecorator {

    private static final long serialVersionUID = 3982296337238205814L;

    protected Corpus corpus;

    public AbstractCorpusDecorator(Corpus corpus) {
        this.corpus = corpus;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    @Override
    public int getNumberOfDocuments() {
        return corpus.getNumberOfDocuments();
    }

    @Override
    public Document getDocument(int documentId) {
        return corpus.getDocument(documentId);
    }

    @Override
    public void addDocument(Document document) {
        corpus.addDocument(document);
    }

    @Override
    public void clear() {
        corpus.clear();
    }

    @Override
    public Iterator<Document> iterator() {
        return corpus.iterator();
    }

    @Override
    public List<Document> getDocuments(int startId, int endId) {
        return corpus.getDocuments(startId, endId);
    }

    public Iterator<Class<? extends CorpusProperty>> getPropertiesIterator() {
        return corpus.getPropertiesIterator();
    }

    public void removeProperty(Class<? extends CorpusProperty> propertyClass) {
        corpus.removeProperty(propertyClass);
    }

    public <T extends CorpusProperty> void addProperty(T property) {
        corpus.addProperty(property);
    }

    public <T extends CorpusProperty> T getProperty(Class<T> propertyClass) {
        return corpus.getProperty(propertyClass);
    }
    
    public HashMap<Class<? extends CorpusProperty>, CorpusProperty> getProperties() {
        return corpus.getProperties();
    }

    public void setProperties(HashMap<Class<? extends CorpusProperty>, CorpusProperty> properties) {
        corpus.setProperties(properties);
    }
}
