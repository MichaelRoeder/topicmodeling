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

@Deprecated
public class DecoratedCorpusHelper {

    private DecoratedCorpusHelper() {
    }

    public static boolean isUndecoratedCorpusInstanceOf(Corpus corpus,
            Class<? extends Corpus> clazz) {
        // Corpus corpusInstance = corpus;
        // while (AbstractCorpusDecorator.class.isInstance(corpusInstance)) {
        // corpusInstance = ((AbstractCorpusDecorator) corpusInstance)
        // .getCorpus();
        // }
        return clazz.isInstance(getUndecoratedCorpus(corpus));
    }

    public static Corpus getUndecoratedCorpus(Corpus corpus) {
        Corpus corpusInstance = corpus;
        while (CorpusDecorator.class.isInstance(corpusInstance)) {
            corpusInstance = ((CorpusDecorator) corpusInstance).getCorpus();
        }
        return corpusInstance;
    }

    public static <T extends Corpus> boolean isCorpusInstanceOf(Corpus corpus,
            Class<T> clazz) {
        return getCorpusInstanceOf(corpus, clazz) != null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Corpus> T getCorpusInstanceOf(Corpus corpus,
            Class<T> clazz) {
        if (clazz.isInstance(corpus)) {
            return (T) corpus;
        }
        Corpus corpusInstance = corpus;
        while (CorpusDecorator.class.isInstance(corpusInstance)) {
            corpusInstance = ((CorpusDecorator) corpusInstance).getCorpus();
            if (clazz.isInstance(corpusInstance)) {
                return (T) corpusInstance;
            }
        }
        return null;
    }
}
