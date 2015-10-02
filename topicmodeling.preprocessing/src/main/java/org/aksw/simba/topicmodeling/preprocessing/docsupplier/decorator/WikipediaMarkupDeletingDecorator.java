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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Deprecated
public class WikipediaMarkupDeletingDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaMarkupDeletingDecorator.class);

    public WikipediaMarkupDeletingDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null)
        {
            LOGGER.error("Got a Document without a DocumentText property!");
            return null;
        }
        text.setText(removeWikiMarkup(text.getText()));
        return document;
    }

    private String removeWikiMarkup(String text) {
        String cleanText = text;
        if (cleanText.startsWith("#")) {
            String temp = cleanText.substring(0, cleanText.length() < 14 ? cleanText.length() : 14);
            temp = temp.toLowerCase();
            if (temp.startsWith("#redirect")) {
                cleanText = cleanText.substring(9);
            } else if (temp.startsWith("#weiterleitung")) {
                cleanText = cleanText.substring(14);
            }
        }

        // makros
        cleanText = cleanTagDeleteContent("\\{\\{", "\\}\\}", cleanText);
        // internal links
        cleanText = cleanTagRetainContent("\\[\\[[^\\]]*\\|", "\\]\\]", cleanText);
        cleanText = cleanTagRetainContent("\\[\\[", "\\]\\]", cleanText);
        // external links
        cleanText = cleanTagRetainContent("\\[[^\\]]* ", "\\]", cleanText);
        cleanText = cleanTagRetainContent("\\[", "\\]", cleanText);
        // comments
        cleanText = cleanTagDeleteContent("<!--", "-->", cleanText, " ");

        // all types of tags
        cleanText = cleanTagRetainContent("<[^>]*>", "</[^>]*>", cleanText, " ");
        // headlines
        cleanText = cleanTagRetainContent("([=]{2,6})", "([=]{2,6})", cleanText);
        // formatings
        cleanText = cleanTagRetainContent("([']{2,5})", "([']{2,5})", cleanText);

        // all types of lists (bullet list, numbered list, definition list, indent text)
        cleanText = cleanTag("\\n[*#;:]+", cleanText, "\n");
        // cleanText = cleanTag("\\n[#]+", cleanText, "\n");
        // horizontal rule
        cleanText = cleanTag("----", cleanText);

        // tables starting with "{| text to be deleted"
        cleanText = cleanTag("\\n\\{\\|[^\\n]*\\n", cleanText, "\n");
        // "! text to be deleted | text to be retained \n"
        cleanText = cleanTag("\\n\\![^\\|\\n]*\\|", cleanText, "\n");
        // "! text to be deleted \n"
        cleanText = cleanTag("\\n\\![^\\|\\n]*", cleanText, "\n");
        // end of table
        cleanText = cleanTag("\\n\\|\\}", cleanText, "\n");
        // new table line
        cleanText = cleanTag("\\n\\|-[^\\n]*\\n", cleanText, "\n");
        // new tabel cell
        cleanText = cleanTag("\\n\\|", cleanText, "\n");

        // unescape symbols which are written with HTML escaping
        cleanText = unescapeSymbols(cleanText);

        return cleanText;
    }

    private static String cleanTagDeleteContent(final String beginPattern, final String endPattern, final String text) {
        return cleanTagDeleteContent(beginPattern, endPattern, text, null);
    }

    private static String cleanTagDeleteContent(final String beginPattern, final String endPattern, final String text,
            final String replacement) {

        String s = "(" + beginPattern + ")|(" + endPattern + ")";
        Pattern pat = Pattern.compile(s);
        Matcher matchRef = pat.matcher(text);

        String cleanText = "";
        String rep = (replacement == null) ? "" : replacement;

        int isRef = 0;
        int refBegin = 0;
        int refEnd = 0;

        while (matchRef.find()) {
            if (matchRef.group().matches(beginPattern)) {
                // System.out.println("found a " + beginPattern + "!!");
                if (isRef == 0) {
                    refEnd = matchRef.start();
                    cleanText += text.substring(refBegin, refEnd) + rep;
                }
                isRef++;
            }
            if (matchRef.group().matches(endPattern)) {
                // System.out.println("found a " + endPattern + "!!");
                isRef--;
                if (isRef == 0) {
                    refBegin = matchRef.end();
                }
            }
        }
        if (matchRef.hitEnd()) {
            cleanText += text.substring(refBegin, text.length());
        }
        return cleanText;
    }

    private static String cleanTag(final String pattern, final String text) {
        return cleanTagRetainContent(pattern, null, text);
    }

    private static String cleanTag(final String pattern, final String text, final String replacement) {
        return cleanTagRetainContent(pattern, null, text, replacement);
    }

    private static String cleanTagRetainContent(final String beginPattern, final String endPattern, final String text) {
        return cleanTagRetainContent(beginPattern, endPattern, text, null);
    }

    private static String cleanTagRetainContent(final String beginPattern, final String endPattern, final String text,
            final String replacement) {
        String rep = (replacement == null) ? "" : replacement;
        String cleanText = text;
        if (beginPattern != null) {
            cleanText = cleanText.replaceAll(beginPattern, rep);
        }
        if (endPattern != null) {
            cleanText = cleanText.replaceAll(endPattern, rep);
        }
        return cleanText;
    }

    private static String unescapeSymbols(final String text) {
        Pattern pat = Pattern.compile("(&[#\\p{Alnum}][\\p{Alnum}]*;)");
        Matcher matchRef = pat.matcher(text);
        StringBuilder cleanText = new StringBuilder();

        int textPos = 0;

        while (matchRef.find()) {
            cleanText.append(text.substring(textPos, matchRef.start()));
            cleanText.append(StringEscapeUtils.unescapeHtml4(text.substring(matchRef.start(), matchRef.end())));
            textPos = matchRef.end();
        }
        cleanText.append(text.substring(textPos));
        return cleanText.toString();
    }
}
