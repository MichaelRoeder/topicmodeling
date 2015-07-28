package org.aksw.simba.topicmodeling.algorithm.mallet;

import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;

import cc.mallet.types.InstanceList;
import cc.mallet.types.MalletAlphabetWrapper;

public class MalletInstanceListWrapper {

    protected InstanceList instanceList;

    MalletInstanceListWrapper(InstanceList instanceList) {
        this.instanceList = instanceList;
    }

    public int getNumberOfDocuments() {
        return instanceList.size();
    }

    public Vocabulary getVocabulary() {
        return new MalletAlphabetWrapper(instanceList.getDataAlphabet());
    }

    public int getNumberOfWords() {
        return instanceList.getAlphabet().size();
    }

    public void clear() {
        instanceList.clear();
    }

}
