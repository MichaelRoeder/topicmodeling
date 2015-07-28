package org.aksw.simba.topicmodeling.lang;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import org.aksw.simba.topicmodeling.lang.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentenceSplitterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentenceSplitterFactory.class);

    public static SentenceDetectorME createSentenceSplitter(Language lang) {
        SentenceDetectorME detector = null;

        String modelName;
        switch (lang) {
        case ENG: {
            modelName = "en-sent.bin";
            break;
        }
        case GER: {
            modelName = "de-sent.bin";
            break;
        }
        default:
            LOGGER.error("Error. couldn't identify model for unknown language " + lang.toString() + ". Returning null.");
            return null;
        }

        InputStream modelIn = SentenceSplitterFactory.class.getClassLoader().getResourceAsStream(modelName);
        if (modelIn == null) {
            LOGGER.error("Couldn't find needed model as resource \"" + modelName + "\". Returning null.");
            return null;
        }
        SentenceModel model = null;
        try {
            model = new SentenceModel(modelIn);
        } catch (Exception e) {
            LOGGER.error("Couldn't load sentence model. Returning null.");
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (IOException e) {
                }
            }
        }
        if (model != null) {
            detector = new SentenceDetectorME(model);
        }
        return detector;
    }
}
