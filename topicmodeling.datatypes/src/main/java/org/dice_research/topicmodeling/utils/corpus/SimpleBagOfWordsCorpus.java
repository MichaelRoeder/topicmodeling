/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.utils.corpus;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Deprecated
public class SimpleBagOfWordsCorpus extends AbstractCorpusWithVocabulary implements BagOfWordsCorpus, CorpusDecorator {

    private static final long serialVersionUID = -5105469729480651080L;

    private static final Logger logger = LoggerFactory
            .getLogger(SimpleBagOfWordsCorpus.class);

    protected Vector<DocumentWordCounts> wordCountsList;

    protected DocumentListCorpus<Vector<Document>> documentList;

    public SimpleBagOfWordsCorpus(Vocabulary vocabulary) {
        super(vocabulary);
        wordCountsList = new Vector<DocumentWordCounts>();
        documentList = new DocumentListCorpus<Vector<Document>>(new Vector<Document>());
    }

    public void increaseWordCount(String word, int documentId) {
        increaseWordCount(word, documentId, 1);
    }

    public void increaseWordCount(String word, int documentId, int count) {
        // If this document is unknown
        if (documentId >= wordCountsList.size()) {
            logger.warn("Got an unknown document with the id " + documentId
                    + ". Try to add it. ("
                    + wordCountsList.size() + ")");
            addDocument(new Document(documentId));
        }

        Integer wordId = vocabulary.getId(word);
        // If this word is unknown
        if (wordId < 0) {
            wordCountsList.get(documentId).setWordCount(vocabulary.size(), count);
            vocabulary.add(word);
        } else {
            wordCountsList.get(documentId).increaseWordCount(wordId, count);
        }
    }

    public int getWordCount(int wordId, int documentId) {
        return wordCountsList.get(documentId).getCountForWord(wordId);
    }

    public int getNumberOfDocuments() {
        return wordCountsList.size();
    }

    public DocumentWordCounts getWordCountsForDocument(int documentId) {
        return wordCountsList.get(documentId);
    }

    @Override
    public Document getDocument(int documentId) {
        return documentList.getDocument(documentId);
    }

    @Override
    public void addDocument(Document document) {
        documentList.addDocument(document);
        DocumentWordCounts counts = document.getProperty(DocumentWordCounts.class);
        if (counts == null) {
            counts = new DocumentWordCounts();
            document.addProperty(counts);
        }
        wordCountsList.add(counts);
    }

    @Override
    public Corpus getCorpus() {
        return documentList;
    }

    @Override
    public void clear() {
        documentList.clear();
        wordCountsList.clear();
    }

    @Override
    public Iterator<Document> iterator() {
        return documentList.iterator();
    }

    @Override
    public List<Document> getDocuments(int startId, int endId) {
        return documentList.getDocuments(startId, endId);
    }

    // @Override
    // public void addDocument(int documentId) {
    // if (documentId != wordDocumentMatrix.size()) {
    // logger.warn("I'm adding the document with the Id " + documentId
    // + " while I was waiting for a document with the id "
    // + wordDocumentMatrix.size());
    // }
    // wordDocumentMatrix.add(new DocumentWordCounts(vocabulary));
    // }
}
