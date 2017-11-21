package org.dice_research.topicmodeling.wikipedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.Charsets;
import org.dice_research.topicmodeling.io.xml.XMLParserObserver;
import org.dice_research.topicmodeling.io.xml.stream.SimpleReaderBasedXMLParser;
import org.dice_research.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaArticleId;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaNamespace;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaRedirect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikipediaDumpReader extends AbstractDocumentSupplier implements XMLParserObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaDumpReader.class);

    private static final String DOCUMENT_XML_TAG_NAME = "page";
    private static final String TITLE_XML_TAG_NAME = "title";
    private static final String TEXT_XML_TAG_NAME = "text";
    private static final String ARTICLE_ID_XML_TAG_NAME = "id";
    private static final String REVISION_XML_TAG_NAME = "revision";
    private static final String REDIRECT_XML_TAG_NAME = "redirect";
    private static final String NAMESPACE_XML_TAG_NAME = "ns";

    private Reader dumpReader;
    private SimpleReaderBasedXMLParser xmlParser;
    private String data;
    private Document document;
    private String lastIdContainingTag;

    public static WikipediaDumpReader createReader(String filename) throws FileNotFoundException {
        return createReader(new File(filename));
    }

    public static WikipediaDumpReader createReader(String filename, Charset charset) throws FileNotFoundException {
        return createReader(new File(filename), charset);
    }

    public static WikipediaDumpReader createReader(File file) throws FileNotFoundException {
        return createReader(file, Charsets.UTF_8);
    }

    public static WikipediaDumpReader createReader(File file, Charset charset) throws FileNotFoundException {
        return createReader(new FileInputStream(file), charset);
    }

    public static WikipediaDumpReader createReader(InputStream input, Charset charset) {
        Reader reader = new InputStreamReader(input, charset);
        return new WikipediaDumpReader(reader);
    }

    private WikipediaDumpReader(Reader reader) {
        this.dumpReader = reader;
        xmlParser = new SimpleReaderBasedXMLParser(reader, this);
    }

    @Override
    public Document getNextDocument() {
        if (dumpReader != null) {
            xmlParser.parse();
            if (document != null) {
                Document nextDocument = document;
                document = null;
                if (LOGGER.isInfoEnabled() && ((nextDocument.getDocumentId() % 1000) == 999)) {
                    LOGGER.info("Read the " + (nextDocument.getDocumentId() + 1) + "th document from the dump.");
                }
                return nextDocument;
            } else {
                // The parser has reached the end of the file
                try {
                    dumpReader.close();
                } catch (IOException e) {
                    LOGGER.error("Error while closing the file reader used for reading the wikipedia dump file.", e);
                }
                dumpReader = null;
            }
        }
        return null;
    }

    @Override
    public void handleOpeningTag(String tagString) {
        if (tagString.startsWith(DOCUMENT_XML_TAG_NAME)) {
            document = new Document(getNextDocumentId());
            lastIdContainingTag = DOCUMENT_XML_TAG_NAME;
        } else if (tagString.startsWith(REVISION_XML_TAG_NAME)) {
            lastIdContainingTag = REVISION_XML_TAG_NAME;
        }
    }

    @Override
    public void handleClosingTag(String tagString) {
        if (tagString.startsWith(DOCUMENT_XML_TAG_NAME)) {
            xmlParser.stop();
        } else if (tagString.startsWith(TITLE_XML_TAG_NAME)) {
            if (document != null) {
                document.addProperty(new DocumentName(data));
            } else {
                LOGGER.error("Found a title tag while there is no document object. Ignoring this title.");
            }
        } else if (tagString.startsWith(TEXT_XML_TAG_NAME)) {
            if (document != null) {
                document.addProperty(new DocumentText(data));
            } else {
                LOGGER.error("Found a text tag while there is no document object. Ignoring this text.");
            }
        } else if (tagString.startsWith(ARTICLE_ID_XML_TAG_NAME) && (lastIdContainingTag == DOCUMENT_XML_TAG_NAME)) {
            // if this closing id tag is inside the document and not inside the revision tag
            if (document != null) {
                try {
                    document.addProperty(new WikipediaArticleId(Integer.parseInt(data)));
                } catch (NumberFormatException e) {
                    LOGGER.error("Found an article id tag but couldn't parse the id. Ignoring this id.", e);
                }
            } else {
                LOGGER.error("Found an article id tag while there is no document object. Ignoring this id.");
            }
        } else if (tagString.startsWith(NAMESPACE_XML_TAG_NAME)) {
            if (document != null) {
                try {
                    document.addProperty(new WikipediaNamespace(Integer.parseInt(data)));
                } catch (NumberFormatException e) {
                    LOGGER.error("Found a namespace tag but couldn't parse the id. Ignoring this namespace.", e);
                }
            } else {
                LOGGER.error("Found a namespace tag while there is no document object. Ignoring this namespace.");
            }
        }
    }

    @Override
    public void handleData(String data) {
        this.data = data;
    }

    @Override
    public void handleEmptyTag(String tagString) {
        if (tagString.startsWith(REDIRECT_XML_TAG_NAME)) {
            if (document != null) {
                String redirect = null;
                int startPos = tagString.indexOf("title=\"");
                if (startPos > 0) {
                    startPos += 7;
                    int endPos = tagString.indexOf('"', startPos);
                    if (endPos > startPos) {
                        redirect = tagString.substring(startPos, endPos);
                    }
                }
                if (redirect == null) {
                    LOGGER.warn("Found a redirect tag but couldn't parse the title (tag=\"" + tagString
                            + "\"). Adding an empty title instead.");
                    redirect = "";
                }
                document.addProperty(new WikipediaRedirect(redirect));
            } else {
                LOGGER.error("Found a redirect tag while there is no document object. Ignoring this redirect.");
            }
        }
    }
}
