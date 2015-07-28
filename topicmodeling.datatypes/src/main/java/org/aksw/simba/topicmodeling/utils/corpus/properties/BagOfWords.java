package org.aksw.simba.topicmodeling.utils.corpus.properties;

import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BagOfWords extends AbstractDocumentPropertySummarizingCorpusProperty<DocumentWordCounts> implements
        CorpusProperty {

    private static final long serialVersionUID = 4311229401025071797L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BagOfWords.class);

    public BagOfWords(Corpus corpus) {
        super(corpus, DocumentWordCounts.class);
    }

    // public void increaseWordCount(int wordId, int documentId) {
    // increaseWordCount(wordId, documentId, 1);
    // }
    //
    // public void increaseWordCount(int wordId, int documentId, int count) {
    // DocumentWordCounts counts = getWordCountsForDocument(documentId);
    // if (counts == null) {
    // LOGGER.error("Couldn't increase a word count for document #"
    // + documentId
    // + " because the document had no DocumentWordCounts property while this CorpusProperty has been created.");
    // } else {
    // counts.increaseWordCount(wordId, count);
    // }
    // }

    public int getWordCount(int wordId, int documentId) {
        DocumentWordCounts counts = getDocumentProperty(documentId);
        if (counts == null) {
            LOGGER.error("Couldn't return a word count for document #" + documentId
                    + " because the document had no DocumentWordCounts property. Returning 0.");
            return 0;
        } else {
            return counts.getCountForWord(wordId);
        }
    }
}
