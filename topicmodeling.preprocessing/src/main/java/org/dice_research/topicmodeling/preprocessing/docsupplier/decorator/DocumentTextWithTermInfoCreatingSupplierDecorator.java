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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.List;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWithTermInfo;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DocumentTextWithTermInfoCreatingSupplierDecorator extends
        AbstractPropertyAppendingDocumentSupplierDecorator<DocumentTextWithTermInfo> {

    public static final char TERM_START_CHAR = '[';
    public static final char TERM_END_CHAR = ']';
    public static final char SEPARATION_CHAR = '|';
    public static final char ESCAPE_CHAR = '\\';

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DocumentTextWithTermInfoCreatingSupplierDecorator.class);

    public DocumentTextWithTermInfoCreatingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected DocumentTextWithTermInfo createPropertyForDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            LOGGER.error("Got a document without the needed DocumentText property. Ignoring this document.");
            return null;
        }
        TermTokenizedText tttext = document.getProperty(TermTokenizedText.class);
        if (tttext == null) {
            LOGGER.error("Got a document without the needed TermTokenizedText property. Ignoring this document.");
            return null;
        }
        return createTextWithTermInfo(text, tttext);
    }

    private DocumentTextWithTermInfo createTextWithTermInfo(DocumentText text, TermTokenizedText tttext) {
        String origText = text.getText();
        StringBuilder textWithInfo = new StringBuilder(origText.length());
        int posInText, foundPos;
        posInText = 0;
        List<Term> terms = tttext.getTermTokenizedText();
        for (Term term : terms) {
            // Search the current term inside the text
            foundPos = origText.indexOf(term.getWordForm(), posInText);
            // If this term hasn't been found
            if (foundPos < 0) {
                LOGGER.error("Couldn't find a term inside the given text. Ignoring this term.\nTerm = "
                        + term.toString()
                        + "\nremaining text = \""
                        + ((origText.length() - posInText) < 50 ? origText.substring(posInText) : origText.substring(
                                posInText, posInText + 50) + "...") + "\".");
            } else {
                // If there is some text not represented by the terms
                if (posInText < foundPos) {
                    // textWithInfo.append(StringEscapeUtils.escapeXml(origText.substring(posInText,
                    // foundPos)));
                    textWithInfo.append(escapeString(origText.substring(posInText, foundPos)));
                }
                // Add the term to the new text
                textWithInfo.append(TERM_START_CHAR);
                textWithInfo.append(escapeString(term.getWordForm()));
                textWithInfo.append(SEPARATION_CHAR);
                textWithInfo.append(escapeString(term.getLemma()));
                textWithInfo.append(SEPARATION_CHAR);
                textWithInfo.append(escapeString(term.getPosTag()));
                textWithInfo.append(SEPARATION_CHAR);
                textWithInfo.append(Long.toString(term.properties.getAsLong()));
                textWithInfo.append(TERM_END_CHAR);

                posInText = foundPos + term.getWordForm().length();
            }
        }
        if (posInText < origText.length()) {
            // textWithInfo.append(StringEscapeUtils.escapeXml(origText.substring(posInText)));
            textWithInfo.append(escapeString(origText.substring(posInText)));
        }
        return new DocumentTextWithTermInfo(textWithInfo.toString());
    }

    public static String escapeString(String value) {
        char chars[] = value.toCharArray();
        int start = 0, pos;
        StringBuilder escapedString = new StringBuilder();
        boolean foundAChar = false;
        for (pos = 0; pos < chars.length; ++pos) {
            switch (chars[pos]) {
            case TERM_START_CHAR: // falls through
            case TERM_END_CHAR:
            case SEPARATION_CHAR:
            case ESCAPE_CHAR: {
                escapedString.append(value.substring(start, pos));
                escapedString.append(ESCAPE_CHAR);
                start = pos;
                foundAChar = true;
                break;
            }
            default: {
                // nothing to do
            }
            }
        }
        if (foundAChar) {
            if (start < value.length()) {
                escapedString.append(value.substring(start));
            }
            return escapedString.toString();
        } else {
            return value;
        }
    }
}
