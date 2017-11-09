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

import java.util.ArrayList;
import java.util.List;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWithTermInfo;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DocumentTextWithTermInfoParsingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DocumentTextWithTermInfoParsingSupplierDecorator.class);

    private static final String DEPRECATED_ENCODING_START = "<term><label>";

    // private MultiPatternAutomaton automaton;
    // private String currentTextWithTermInfo;
    // private StringBuilder currentDocumentText;
    // private Vector<Term> currentTerms;
    // private int lastPosInString;

    public DocumentTextWithTermInfoParsingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
        // automaton = new BricsAutomatonManager(this, new String[] {
        // //ESCAPED_CHAR_PATTERN,
        // TERM_PATTERN });
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentTextWithTermInfo textWithTermInfo = document.getProperty(DocumentTextWithTermInfo.class);
        if (textWithTermInfo != null) {
            createTextAndTerms(textWithTermInfo, document);
        } else {
            LOGGER.error("Got a document without the needed DocumentTextWithTermInfo property. Ignoring this document.");
            return null;
        }
        return document;
    }

    private boolean containsForDeprecatedEncode(String textWithTermInfo) {
        return textWithTermInfo.contains(DEPRECATED_ENCODING_START);
    }

    private void createTextAndTerms(DocumentTextWithTermInfo textWithTermInfo, Document document) {
        String currentTextWithTermInfo = textWithTermInfo.getTextWithTerms();
        if (containsForDeprecatedEncode(currentTextWithTermInfo)) {
            LOGGER.warn("The DocumentTextWithTermInfo value contains a deprecated encoding. It is possible that the parsing won't find any terms.");
        }

        StringBuilder documentText = new StringBuilder();
        TermTokenizedText tttext = new TermTokenizedText();
        List<Term> currentTerms = tttext.getTermTokenizedText();

        char chars[] = currentTextWithTermInfo.toCharArray();

        StringBuilder currentTermPart = new StringBuilder();
        StringBuilder currentStringBuilder = documentText;
        List<String> termParts = new ArrayList<String>(4);
        Term term;
        boolean escaped = false;
        boolean insideTerm = false;
        for (int i = 0; i < chars.length; ++i) {
            switch (chars[i]) {
            case DocumentTextWithTermInfoCreatingSupplierDecorator.ESCAPE_CHAR: {
                if (escaped) {
                    currentStringBuilder.append(chars[i]);
                    escaped = false;
                } else {
                    escaped = true;
                }
                break;
            }
            case DocumentTextWithTermInfoCreatingSupplierDecorator.TERM_START_CHAR: {
                if (escaped) {
                    currentStringBuilder.append(chars[i]);
                    escaped = false;
                } else {
                    if (insideTerm) {
                        LOGGER.warn("Found an unescaped term start character '" + chars[i]
                                + "' inside of a term. It will be handled like an escaped one.");
                        currentStringBuilder.append(chars[i]);
                    } else {
                        insideTerm = true;
                        currentStringBuilder = currentTermPart;
                    }
                }
                break;
            }
            case DocumentTextWithTermInfoCreatingSupplierDecorator.TERM_END_CHAR: {
                if (escaped) {
                    currentStringBuilder.append(chars[i]);
                    escaped = false;
                } else {
                    if (insideTerm) {
                        if (termParts.size() != 3) {
                            LOGGER.warn("Got a term with " + (termParts.size() + 1)
                                    + " instead of the expected 4 parts. The term will be ignored.");
                        } else {
                            term = new Term(termParts.get(0), termParts.get(1));
                            term.setPosTag(termParts.get(2));
                            try {
                                term.properties.set(Long.parseLong(currentStringBuilder.toString()));
                                currentTerms.add(term);
                                documentText.append(term.getWordForm());
                            } catch (Exception e) {
                                LOGGER.error(
                                        "Couldn't parse the properties of the term from \""
                                                + currentStringBuilder.toString() + "\". The term will be ignored.", e);
                            }
                            term = null;
                        }
                        currentStringBuilder = documentText;
                        currentTermPart.delete(0, currentTermPart.length());
                        termParts.clear();
                        insideTerm = false;
                    } else {
                        LOGGER.warn("Found an unescaped term start character '" + chars[i]
                                + "' inside of a term. It will be handled like an escaped one.");
                        currentStringBuilder.append(chars[i]);
                    }
                }
                break;
            }
            case DocumentTextWithTermInfoCreatingSupplierDecorator.SEPARATION_CHAR: {
                if (escaped) {
                    currentStringBuilder.append(chars[i]);
                    escaped = false;
                } else {
                    if (insideTerm) {
                        termParts.add(currentTermPart.toString());
                        currentTermPart.delete(0, currentTermPart.length());
                    } else {
                        LOGGER.warn("Found an unescaped separation character '" + chars[i]
                                + "' outside of a term. It will be handled like an escaped one.");
                        currentStringBuilder.append(chars[i]);
                    }
                }
                break;
            }
            default: {
                currentStringBuilder.append(chars[i]);
            }
            }
        }

        if ((currentTermPart.length() > 0) || (termParts.size() > 0)) {
            LOGGER.warn("There was an unclosed term at the end of the text. It will be ignored.");
        }

        document.addProperty(new DocumentText(documentText.toString()));
        document.addProperty(tttext);
    }

    // private Term parseTerm(String termString) {
    // int start = 1, end = termString.length() - 1;
    // List<String> strings = new ArrayList<String>(4);
    // char chars[] = termString.toCharArray();
    // StringBuilder currentString = new StringBuilder(chars.length);
    // boolean escaped = false;
    // for (int i = start; i < end; ++i) {
    // switch (chars[i]) {
    // case DocumentTextWithTermInfoCreatingSupplierDecorator.ESCAPE_CHAR: {
    // if (escaped) {
    // currentString.append(chars[i]);
    // escaped = false;
    // } else {
    // escaped = true;
    // }
    // break;
    // }
    // case DocumentTextWithTermInfoCreatingSupplierDecorator.SEPARATION_CHAR: {
    // if (escaped) {
    // currentString.append(chars[i]);
    // escaped = false;
    // } else {
    // strings.add(currentString.toString());
    // currentString.delete(0, currentString.length());
    // start = i + 1;
    // }
    // break;
    // }
    // default: {
    // currentString.append(chars[i]);
    // }
    // }
    // }
    //
    // strings.add(currentString.toString());
    //
    // if (strings.size() != 4) {
    // LOGGER.warn("Got a term with " + strings.size() +
    // " instead of the expected 4 parts (\"" + termString
    // + "\"). Returning null.");
    // return null;
    // }
    // Term term = new Term(strings.get(0), strings.get(1));
    // term.setPartOfSpeechTag(strings.get(2));
    // try {
    // parseProperties(term.properties, Integer.parseInt(strings.get(3)));
    // } catch (Exception e) {
    // LOGGER.error("Couldn't parse the properties of the term \"" + termString
    // + "\". Returning null.", e);
    // return null;
    // }
    //
    // return term;
    // }

    // @Override
    // public void foundPattern(int patternId, int startPos, int length) {
    // // append the text in front of this pattern
    // currentDocumentText.append(unescapeString(currentTextWithTermInfo.substring(lastPosInString,
    // startPos)));
    // lastPosInString = startPos + length;
    //
    // // if (patternId == 0) {
    // // // the escaped character will be added to the text the next time
    // lastPosInString will be used
    // // --lastPosInString;
    // // } else {
    // Term term = parseTerm(currentTextWithTermInfo.substring(startPos,
    // lastPosInString));
    // if (term != null) {
    // currentTerms.add(term);
    // currentDocumentText.append(term.getWordForm());
    // }
    // // }
    // }

    public static String unescapeString(String value) {
        char chars[] = value.toCharArray();
        StringBuilder currentString = new StringBuilder(chars.length);
        boolean escaped = false;
        for (int i = 0; i < chars.length; ++i) {
            switch (chars[i]) {
            case DocumentTextWithTermInfoCreatingSupplierDecorator.ESCAPE_CHAR: {
                if (escaped) {
                    currentString.append(chars[i]);
                    escaped = false;
                } else {
                    escaped = true;
                }
                break;
            }
            default: {
                currentString.append(chars[i]);
            }
            }
        }
        return currentString.toString();
    }

}
