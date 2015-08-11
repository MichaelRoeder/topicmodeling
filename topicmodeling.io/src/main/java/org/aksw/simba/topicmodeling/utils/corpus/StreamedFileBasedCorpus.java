package org.aksw.simba.topicmodeling.utils.corpus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aksw.simba.topicmodeling.io.stream.DocumentSupplierDeSerializer;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.AbstractCorpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StreamedFileBasedCorpus extends AbstractCorpus {

    private static final long serialVersionUID = 207762970663404304L;

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamedFileBasedCorpus.class);

    private int numberOfDocuments;
    private File corpusFile;

    public StreamedFileBasedCorpus(File corpusFile, int numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
        this.corpusFile = corpusFile;
    }

    @Override
    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    @Override
    public Document getDocument(int documentId) {
        LOGGER.info("Someone is using my extremly slow getDocument(int id) method.");
        Iterator<Document> iterator = iterator();
        Document document;
        int id = -1;
        while (iterator.hasNext()) {
            document = iterator.next();
            if (id == documentId) {
                return document;
            }
            ++id;
        }
        return null;
    }

    @Override
    public void addDocument(Document document) {
        throw new UnsupportedOperationException("This is a read only corpus. New documents can't be added");
    }

    @Override
    public void clear() {
        corpusFile.delete();
    }

    @Override
    public List<Document> getDocuments(int startId, int endId) {
        LOGGER.info("Someone is using my extremly slow getDocuments(int startId, int endId) method.");

        Iterator<Document> iterator = iterator();
        List<Document> result = new ArrayList<Document>();
        Document document = null;
        ;
        int id = -1;
        while (iterator.hasNext() && (id < startId)) {
            ++id;
            document = iterator.next();
        }

        while (iterator.hasNext() && (id < endId)) {
            result.add(document);
            ++id;
            document = iterator.next();
        }
        return result;
    }

    @Override
    public Iterator<Document> iterator() {
        return new StreamedFileBasedCorpusIterator(DocumentSupplierDeSerializer.create(corpusFile));
    }

    private static class StreamedFileBasedCorpusIterator implements Iterator<Document> {

        private DocumentSupplier supplier;
        private Document nextDocument = null;

        public StreamedFileBasedCorpusIterator(DocumentSupplier next) {
            this.supplier = next;
        }

        @Override
        public boolean hasNext() {
            if (nextDocument == null) {
                nextDocument = supplier.getNextDocument();
            }
            return nextDocument != null;
        }

        @Override
        public Document next() {
            Document result;
            if (nextDocument == null) {
                result = supplier.getNextDocument();
            } else {
                result = nextDocument;
                nextDocument = null;
            }
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("This Iterator can not remove documents.");
        }

    }

}