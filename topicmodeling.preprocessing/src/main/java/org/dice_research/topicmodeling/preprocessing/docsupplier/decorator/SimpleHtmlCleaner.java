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

import org.apache.commons.lang3.StringEscapeUtils;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;


public class SimpleHtmlCleaner extends AbstractDocumentSupplierDecorator {

    private static enum States {
        NORMAL_TEXT, TAG_STARTED, ENCODED_CHAR_STARTED
    }

    public SimpleHtmlCleaner(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        text.setText(clearText(text.getText()));
        return document;
    }

    public String clearText(String text) {
        States state = States.NORMAL_TEXT;
        char c;
        int diffToPos;
        int startPos = 0;
        int lastPos = 0;
        StringBuilder cleanedString = new StringBuilder();
        char chars[] = text.toCharArray();
        for (int i = 0; i < text.length(); ++i) {
            c = chars[i];
            switch (state) {
            case NORMAL_TEXT: {
                switch (c) {
                case '<': {
                    state = States.TAG_STARTED;
                    startPos = i;
                    break;
                }
                case '&': {
                    state = States.ENCODED_CHAR_STARTED;
                    startPos = i;
                    break;
                }
                }
                break;
            }
            case TAG_STARTED: {
                if (c == '>') {
                    cleanedString.append(text.substring(lastPos, startPos));
                    lastPos = i + 1;
                    state = States.NORMAL_TEXT;
                }
                break;
            }
            case ENCODED_CHAR_STARTED: {
                diffToPos = startPos - i;
                if (diffToPos > 7) {
                    // no encoded character has such a long encoding
                    state = States.NORMAL_TEXT;
                    break;
                }
                switch (c) {
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    break;
                }
                case '#': {
                    if (diffToPos > 1) {
                        state = States.NORMAL_TEXT;
                    }
                    break;
                }
                case '<': {
                    state = States.TAG_STARTED;
                    startPos = i;
                    break;
                }
                case ';': {
                    cleanedString.append(text.substring(lastPos, startPos));
                    lastPos = i + 1;
                    cleanedString.append(StringEscapeUtils.unescapeHtml4(text.substring(startPos, lastPos)));
                    state = States.NORMAL_TEXT;
                    break;
                }
                default: {
                    state = States.NORMAL_TEXT;
                }
                }
                break;
            }
            } // switch (state) {
        } // for (int i = 0; i < text.length(); ++i)
        cleanedString.append(text.substring(lastPos));
        return cleanedString.toString();
    }
}
