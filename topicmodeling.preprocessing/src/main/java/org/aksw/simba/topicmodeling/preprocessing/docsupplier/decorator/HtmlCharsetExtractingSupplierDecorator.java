package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.nio.charset.Charset;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentCharset;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HtmlCharsetExtractingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlCharsetExtractingSupplierDecorator.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public HtmlCharsetExtractingSupplierDecorator(DocumentSupplier documentSource) {
        super(new DocumentTextCreatingSupplierDecorator(documentSource, Charset.forName("ISO-8859-1")));
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null)
        {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        DocumentCharset charset = document.getProperty(DocumentCharset.class);
        if (charset == null)
        {
            charset = new DocumentCharset(DEFAULT_CHARSET);
            document.addProperty(charset);
        }
        StringWithCharset encodedText = checkEncoding(new StringWithCharset(text.getText(), charset.getCharset()));
        if (!charset.getCharset().equals(encodedText.getCharset())) {
            text.setText(encodedText.getString());
            document.addProperty(new DocumentCharset(encodedText.getCharset()));
        }
        return document;
    }

    private StringWithCharset checkEncoding(StringWithCharset text) {
        Charset inTextCharset = extractCharset(text.string);
        if ((inTextCharset != null) && (!text.charset.equals(inTextCharset))) {
            text.changeCharset(inTextCharset);
        }
        return text;
    }

    private Charset extractCharset(String html) {
        String htmlHead = extractLowercasedHead(html);
        if (htmlHead == null) {
            LOGGER.warn("HTML page without <head>. Couldn't extract Charset.");
            return null;
        }
        int metaStart = htmlHead.indexOf("<meta ");
        int metaEnd;
        String charsetName = null;
        // as long as there are meta tags available
        while (metaStart > 0) {
            metaEnd = htmlHead.indexOf(">", metaStart);
            charsetName = extractCharsetFromMetaTag(htmlHead, metaStart, metaEnd);
            if (charsetName != null) {
                try {
                    return Charset.forName(charsetName);
                } catch (Exception e) {
                }
            }
            metaStart = htmlHead.indexOf("<meta ", metaEnd);
        }
        return null;
    }

    private String extractLowercasedHead(String html) {
        int headStart = html.indexOf("<head");
        if (headStart < 0) {
            html = html.toLowerCase();
            headStart = html.indexOf("<head");
            if (headStart < 0) {
                return null;
            }
        }
        int headEnd = html.indexOf("</head>", headStart);
        if (headEnd < 0) {
            headEnd = html.indexOf("<body", headStart);
            if (headStart < 0) {
                return null;
            }
        }
        return html.substring(headStart, headEnd).toLowerCase();
    }

    private String extractCharsetFromMetaTag(String html, int metaStart, int metaEnd) {
        int start, end;
        // check the name of the meta tag
        start = html.indexOf("name", metaStart);
        // if there is no name look for http-equiv
        if ((start < 0) || (start > metaEnd)) {
            start = html.indexOf("http-equiv", metaStart);
        }
        // if there is no name and no http-equiv
        if ((start < 0) || (start > metaEnd)) {
            // maybe the charset is defined directly (<meta charset=utf-8>)
            start = html.indexOf("charset=", metaStart);
            if ((start < 0) || (start > metaEnd)) {
                return null;
            }
            start += 8; // += length of "charset="
            char c = html.charAt(start);
            if ((c == '"') || (c == '\'')) {
                ++start;
                end = html.indexOf(c, start);
            } else {
                end = html.indexOf(' ', start);
                if ((end < 0) || (end > metaEnd)) {
                    end = html.indexOf('/', start);
                    if ((end < 0) || (end > metaEnd)) {
                        end = html.indexOf('>', start);
                    }
                }
            }
            if ((end < 0) || (end > metaEnd)) {
                return null;
            }
            return html.substring(start, end);
        }
        start = html.indexOf('=', start);
        if ((start < 0) || (start > metaEnd)) {
            return null;
        }
        ++start;
        char c = html.charAt(start);
        if ((c == '"') || (c == '\'')) {
            ++start;
            end = html.indexOf(c, start);
        } else {
            end = html.indexOf(' ', start);
            if ((end < 0) || (end > metaEnd)) {
                end = html.indexOf('/', start);
                if ((end < 0) || (end > metaEnd)) {
                    end = html.indexOf('>', start);
                }
            }
        }
        if ((end < 0) || (end > metaEnd)) {
            return null;
        }
        String metaTagName = html.substring(start, end);
        metaTagName = metaTagName.toLowerCase();
        // check if this is the correct meta tag
        if (metaTagName.equals("content-type")) {
            start = html.indexOf("charset=", metaStart);
            if ((start < 0) || (start > metaEnd)) {
                return null;
            }
            start += 8; // += length of "charset="
            end = html.indexOf(';', start);
            if ((end < 0) || (end > metaEnd)) {
                end = html.indexOf('"', start);
                if ((end < 0) || (end > metaEnd)) {
                    end = html.indexOf('\'', start);
                    if ((end < 0) || (end > metaEnd)) {
                        end = html.indexOf('/', start);
                        if ((end < 0) || (end > metaEnd)) {
                            end = html.indexOf('>', start);
                        }
                    }
                }
            }
            if ((end < 0) || (end > metaEnd)) {
                return null;
            }
            return html.substring(start, end);
        }
        return null;
    }

    public static class StringWithCharset {
        private String string;
        private Charset charset;

        public StringWithCharset(String string, Charset charset) {
            this.string = string;
            this.charset = charset;
        }

        public void setString(String string) {
            this.string = string;
        }

        public void changeCharset(Charset charset) {
            this.string = new String(this.string.getBytes(this.charset), charset);
            this.charset = charset;
        }

        public String getString() {
            return string;
        }

        public Charset getCharset() {
            return charset;
        }
    }
}
