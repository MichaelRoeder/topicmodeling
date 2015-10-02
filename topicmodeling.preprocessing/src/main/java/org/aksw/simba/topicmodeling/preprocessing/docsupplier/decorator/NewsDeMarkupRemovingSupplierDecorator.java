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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.apache.commons.lang3.StringEscapeUtils;


import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

public class NewsDeMarkupRemovingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private RunAutomaton tagAutomaton;
    private RunAutomaton charAutomaton;
    private RunAutomaton tooltipAutomaton;

    public NewsDeMarkupRemovingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
        RegExp regex = new RegExp("\\<[^\\<\\>]*\\>");
        tagAutomaton = new RunAutomaton(regex.toAutomaton());
        regex = new RegExp("\\&[#A-Za-z][A-Za-z]{1,6};");
        charAutomaton = new RunAutomaton(regex.toAutomaton());
        regex = new RegExp("\\[[^\\[\\]]*\\]");
        tooltipAutomaton = new RunAutomaton(regex.toAutomaton());
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        text.setText(cleanText(text.getText()));
        return document;
    }

    public String cleanText(String text) {
        int pos = 0;
        int lastPos = 0;
        int length = 0;
        StringBuilder cleanText = new StringBuilder();
        StringBuilder toolTips = new StringBuilder();
        char chars[] = text.toCharArray();
        while (pos < chars.length) {
            switch (chars[pos]) {
            case '<': {
                length = tagAutomaton.run(text, pos);
                if (length > 0) {
                    cleanText.append(text.substring(lastPos, pos));
                    handleHtmlTag(cleanText, text, pos, length);
                    pos += length;
                    lastPos = pos;
                } else {
                    ++pos;
                }
                break;
            }
            case '&': {
                length = charAutomaton.run(text, pos);
                if (length > 0) {
                    cleanText.append(text.substring(lastPos, pos));
                    handleHtmlEncodedChar(cleanText, text, pos, length);
                    pos += length;
                    lastPos = pos;
                } else {
                    ++pos;
                }
                break;
            }
            case '[': {
                length = tooltipAutomaton.run(text, pos);
                if (length > 0) {
                    cleanText.append(text.substring(lastPos, pos));
                    handleToolTip(cleanText, toolTips, text, pos, length);
                    pos += length;
                    lastPos = pos;
                } else {
                    ++pos;
                }
                break;
            }
            default: {
                ++pos;
                break;
            }
            }
        }
        cleanText.append(text.substring(lastPos));
        cleanText.append(toolTips);
        return cleanText.toString();
    }

    private void handleHtmlTag(StringBuilder cleanText, String text, int pos, int length) {
        String tagName = text.substring(pos + 1, pos + length);
        if (tagName.startsWith("p") || tagName.startsWith("/p") || tagName.startsWith("tr")
                || tagName.startsWith("/tr")) {
            cleanText.append('\n');
        } else {
            cleanText.append(' ');
        }
    }

    private void handleHtmlEncodedChar(StringBuilder cleanText, String text, int pos, int length) {
        cleanText.append(StringEscapeUtils.unescapeHtml4(text.substring(pos, pos + length)));
    }

    private void handleToolTip(StringBuilder cleanText, StringBuilder toolTips, String text, int pos, int length) {
        // if it is not a closing tool tip tag "[/tt]" or an url tag "[url=...]", "[/url]"
        if ((text.charAt(pos + 1) == 't') && (text.charAt(pos + 2) == 't') && (text.charAt(pos + 3) == '=')) {
            // get the tooltip from "[tt=<tooltip>]"
            String tooltip = text.substring(pos + 4, pos + length - 1);
            toolTips.append('\n');
            toolTips.append(tooltip);
        }
        cleanText.append(' ');
    }
}
