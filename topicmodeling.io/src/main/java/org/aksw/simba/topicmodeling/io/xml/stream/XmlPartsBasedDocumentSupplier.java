package org.aksw.simba.topicmodeling.io.xml.stream;

import java.io.File;

import org.aksw.simba.topicmodeling.io.xml.XmlBasedCorpusPartWriter;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlPartsBasedDocumentSupplier extends AbstractDocumentSupplier implements DocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPartsBasedDocumentSupplier.class);

    private static final boolean USE_DOCUMENT_IDS_FROM_FILE_DEFAULT = true;

    private String filePrefix;
    private String fileSuffix;
    private boolean useDocumentIdsFromFile;
    private final File inputFolder;
    private int currentPartId = -1;
    private StreamBasedXmlDocumentSupplier currentReader;

    public XmlPartsBasedDocumentSupplier(File inputFolder) {
        this(inputFolder, XmlBasedCorpusPartWriter.PART_FILE_PREFIX, XmlBasedCorpusPartWriter.PART_FILE_SUFFIX,
                USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix) {
        this(inputFolder, filePrefix, fileSuffix, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix,
            boolean useDocumentIdsFromFile) {
        this.inputFolder = inputFolder;
        this.filePrefix = filePrefix;
        this.fileSuffix = fileSuffix;
        this.useDocumentIdsFromFile = useDocumentIdsFromFile;
    }

    @Override
    public Document getNextDocument() {
        Document document = requestNextDocument();
        if (document != null) {
            if (!useDocumentIdsFromFile) {
                document.setDocumentId(getNextDocumentId());
            }
        }
        return document;
    }

    private Document requestNextDocument() {
        Document document = null;
        if (currentReader != null) {
            document = currentReader.getNextDocument();
        }
        if (document == null) {
            currentReader = getNextReader();
            if (currentReader != null) {
                document = currentReader.getNextDocument();
            }
        }
        return document;
    }

    private StreamBasedXmlDocumentSupplier getNextReader() {
        ++currentPartId;
        File nextFile = new File(inputFolder.getAbsolutePath() + File.separator + filePrefix + currentPartId
                + fileSuffix);
        StreamBasedXmlDocumentSupplier reader = null;
        if (nextFile.exists()) {
            reader = StreamBasedXmlDocumentSupplier.createReader(nextFile, useDocumentIdsFromFile);
            if (reader != null) {
                LOGGER.info("Started reading part " + currentPartId + ".");
            }
        }
        return reader;
    }
}
