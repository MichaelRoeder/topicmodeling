/**
 * This file is part of topicmodeling.lang.
 *
 * topicmodeling.lang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.lang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.lang.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.lang;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import org.dice_research.topicmodeling.lang.Language;
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
