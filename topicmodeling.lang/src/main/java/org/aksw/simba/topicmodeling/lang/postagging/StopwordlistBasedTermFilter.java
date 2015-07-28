package org.aksw.simba.topicmodeling.lang.postagging;

import java.io.File;
import java.io.InputStream;

import org.aksw.simba.topicmodeling.lang.Language;
import org.aksw.simba.topicmodeling.lang.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StopwordlistBasedTermFilter extends AbstractListBasedTermFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopwordlistBasedTermFilter.class);

    public StopwordlistBasedTermFilter(Language lang) {
        switch (lang) {
        case GER: {
            createWordListSafely(this.getClass().getClassLoader()
                    .getResourceAsStream(StandardGermanPosTaggingTermFilter.STOPWORD_FILE));
            break;
        }
        case ENG: {
            createWordListSafely(this.getClass().getClassLoader()
                    .getResourceAsStream(StandardEnglishPosTaggingTermFilter.STOPWORD_FILE));
            break;
        }
        default: {
            LOGGER.warn("There is no default stop word list for the language " + lang.toString()
                    + ". This filter won't work.");
        }
        }
    }

    public StopwordlistBasedTermFilter(File stopwordlistFile) {
        createWordListSafely(stopwordlistFile);
    }

    public StopwordlistBasedTermFilter(InputStream stopwordlistStream) {
        createWordListSafely(stopwordlistStream);
    }

    @Override
    public boolean isTermGood(Term term) {
        return !(isWordInWordlist(term.getLemma()));
    }

}
