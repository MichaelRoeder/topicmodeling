package org.aksw.simba.topicmodeling.preprocessing;

import org.aksw.simba.topicmodeling.lang.Language;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;

public interface PreprocessorFactory {

    // @Deprecated
    // public org.aksw.simba.topicmodeling.preprocessing.Preprocessor
    // createPreprocessor(Language language);

    public Preprocessor createPreprocessor(DocumentSupplier supplier,
            Language language);
}
