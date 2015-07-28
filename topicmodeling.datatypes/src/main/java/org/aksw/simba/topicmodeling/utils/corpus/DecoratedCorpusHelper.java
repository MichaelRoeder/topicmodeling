package org.aksw.simba.topicmodeling.utils.corpus;

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
