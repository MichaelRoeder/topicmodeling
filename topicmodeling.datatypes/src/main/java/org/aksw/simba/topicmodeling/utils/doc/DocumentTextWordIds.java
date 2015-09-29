package org.aksw.simba.topicmodeling.utils.doc;

import java.util.Arrays;

import com.carrotsearch.hppc.IntIntOpenHashMap;

public class DocumentTextWordIds extends AbstractDocumentProperty {

    private static final long serialVersionUID = -7414003911422423930L;

    private int wordIds[];

    public DocumentTextWordIds(int wordIds[]) {
        this.wordIds = wordIds;
    }

    public DocumentTextWordIds(int size) {
        this.wordIds = new int[size];
    }

    @Override
    public Object getValue() {
        return wordIds;
    }

    public int[] getWordIds() {
        return wordIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DocumentTextWordIds) {
            return Arrays.equals(this.wordIds, ((DocumentTextWordIds) obj).wordIds);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(wordIds);
    }

    @Override
    public String toString() {
        return "DocumentTextWordIds=" + Arrays.toString(wordIds);
    }

    /**
     * Generates a DocumentTextWordIds property from the given
     * {@link DocumentWordCounts} property. Note that the word ids inside the
     * generated property are artificial and might be different to the order in
     * the original document.
     * 
     * @param wordCounts
     * @return
     */
    public static DocumentTextWordIds fromSummedWordCounts(DocumentWordCounts wordCounts) {
        if ((wordCounts == null) || (wordCounts.getWordCounts() == null)) {
            return null;
        }
        int sumOfWordCounts = wordCounts.getSumOfWordCounts();
        int tokens[] = new int[sumOfWordCounts];
        int index = 0;
        IntIntOpenHashMap counts = wordCounts.getWordCounts();
        for (int i = 0; i < counts.allocated.length; ++i) {
            if (counts.allocated[i]) {
                for (int j = 0; j < counts.values[i]; ++j) {
                    tokens[index] = counts.keys[i];
                    ++index;
                }
            }
        }
        return new DocumentTextWordIds(tokens);
    }

}
