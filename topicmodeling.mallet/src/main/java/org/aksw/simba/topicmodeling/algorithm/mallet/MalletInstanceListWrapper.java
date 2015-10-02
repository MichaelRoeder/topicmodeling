/**
 * This file is part of topicmodeling.mallet.
 *
 * topicmodeling.mallet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.mallet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.mallet.  If not, see <http://www.gnu.org/licenses/>.
 */
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
