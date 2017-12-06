package org.dice_research.topicmodeling.preprocessing.corpus;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.StreamSupport;

import org.dice_research.topicmodeling.io.json.JsonPartsBasedDocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.consume.DocumentFrequencyDeterminer;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplierAsIterator;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.VocabularyReductionMappingApplyingSupplierDecorator;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.corpus.DocumentListCorpus;
import org.dice_research.topicmodeling.utils.corpus.properties.CorpusVocabulary;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;

import com.carrotsearch.hppc.BitSet;

/**
 * A preprocessor that removes words that occur either too often or too rare in
 * the corpus from the {@link DocumentTextWordIds} of the documents and from the
 * {@link Vocabulary}. It is assumed that the corpus has a
 * {@link CorpusVocabulary} property.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class DocumentFrequencyBasedCorpusPreprocessor implements CorpusPreprocessor {

    private int minDF = 0;
    private int maxDF = Integer.MAX_VALUE;

    @Override
    public Corpus preprocess(Corpus corpus) {
        Vocabulary vocabulary = corpus.getProperty(CorpusVocabulary.class).get();
        DocumentFrequencyDeterminer dfDeterminer = new DocumentFrequencyDeterminer(vocabulary);
        StreamSupport.stream(Spliterators.spliterator(corpus.iterator(), corpus.getNumberOfDocuments(),
                Spliterator.DISTINCT & Spliterator.NONNULL), true).forEach(dfDeterminer);
        // Create new wordIds
        int mapping[] = createWordIdMapping(vocabulary, dfDeterminer.getCounts(), minDF, maxDF);
        
    }

    public static int[] createWordIdMapping(Vocabulary vocabulary, AtomicIntegerArray counts, int minDF, int maxDF) {
        BitSet keptWords = new BitSet(counts.length());
        int count;
        for (int i = 0; i < counts.length(); ++i) {
            count = counts.get(i);
            if ((count >= minDF) && (count <= maxDF)) {
                keptWords.set(i);
            }
        }
        return VocabularyReductionMappingApplyingSupplierDecorator.createMapping(vocabulary, keptWords);
    }

}
