/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.splitter;

import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.util.Span;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.dice_research.topicmodeling.utils.doc.DocumentSentenceBoundary;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentenceBasedDocumentTextSplitter extends AbstractSplittingDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentenceBasedDocumentTextSplitter.class);

    private SentenceDetectorME sentenceDetector;

    public SentenceBasedDocumentTextSplitter(DocumentSupplier documentSource, SentenceDetectorME sentenceDetector) {
        super(documentSource);
        this.sentenceDetector = sentenceDetector;
    }

    @Override
    protected void splitDocument(Document document) {
        if (document == null) {
            return;
        }
        DocumentText docText = document.getProperty(DocumentText.class);
        if (docText == null) {
            LOGGER.info("Got a document without the needed DocumentText property. Ignoring this document.");
            return;
        }
        TermTokenizedText tttext = document.getProperty(TermTokenizedText.class);
        if (tttext == null) {
            splitDocument(document, docText.getText());
        } else {
            splitDocument(document, docText.getText(), tttext.getTermTokenizedText());
        }
    }

    private void splitDocument(Document document, String text, List<Term> terms) {
        Span positions[] = sentenceDetector.sentPosDetect(text);
        if (positions.length == 0) {
            return;
        }
        int termPosInText, posInText = 0, termId = 0, currentSentenceId = 0;
        Term term = null;
        TermTokenizedText termsOfSentence = new TermTokenizedText();
        // Map all terms to their sentence
        while (termId < terms.size()) {
            term = terms.get(termId);
            termPosInText = text.indexOf(term.getWordForm(), posInText);
            if (termPosInText >= 0) {
                // we have reached the end of this sentence
                while (termPosInText > positions[currentSentenceId].getEnd()) {
                    this.queue.add(createDocument(text, positions[currentSentenceId].getStart(),
                            positions[currentSentenceId].getEnd(), termsOfSentence, document));
                    ++currentSentenceId;
                    if (currentSentenceId >= positions.length) {
                        LOGGER.warn("If have seen all sentences. But I still have unprocessed terms. They will be lost.");
                        return;
                    }
                    termsOfSentence = new TermTokenizedText();
                }
                termsOfSentence.addTerm(term);
                posInText = termPosInText + term.getWordForm().length();
            } else {
                LOGGER.error("Couldn't find a term inside the given text. Ignoring this term.\nTerm = "
                        + term.toString() + "\nremaining text = \"" + text.substring(posInText) + "\".");
            }
            ++termId;
        }
        // Finish the last sentence
        this.queue.add(createDocument(text, positions[currentSentenceId].getStart(),
                positions[currentSentenceId].getEnd(), termsOfSentence, document));
        ++currentSentenceId;

        // Create all remaining sentences which do not contain any terms
        while (currentSentenceId < positions.length) {
            this.queue.add(createDocument(text, positions[currentSentenceId].getStart(),
                    positions[currentSentenceId].getEnd(), new TermTokenizedText(), document));
            ++currentSentenceId;
        }
    }

    private void splitDocument(Document document, String text) {
        Span positions[] = sentenceDetector.sentPosDetect(text);
        for (int i = 0; i < positions.length; ++i) {
            if (text.substring(positions[i].getStart(), positions[i].getEnd()).trim().length() > 0) {
                this.queue.add(createDocument(text, positions[i].getStart(), positions[i].getEnd(), null, document));
            }
        }
    }

    private Document createDocument(String documentText, int start, int end, TermTokenizedText tttext, Document document) {
        Document newDocument = new Document(getNextDocumentId());
        for (DocumentProperty property : document) {
            newDocument.addProperty(property);
        }
        newDocument.addProperty(new DocumentText(documentText.substring(start, end)));
        if (tttext != null) {
            newDocument.addProperty(tttext);
        }
        newDocument.addProperty(new DocumentSentenceBoundary(start, end - start));
        return newDocument;
    }

}
