package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.filter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StopwordBasedEntityFilter implements NamedEntitiesFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopwordBasedEntityFilter.class);

    private static final int MINIMUM_LENGTH_OF_NE = 2;

    private Set<String> stopwordlist;

    public StopwordBasedEntityFilter() {
        createWordList(new File(this.getClass().getClassLoader().getResource("englishStopwordlist.txt")
                .toString().replace("file:", "")));
    }

    protected void createWordList(File stopwordfile) {
        try {
            stopwordlist = new HashSet<String>(FileUtils.readLines(stopwordfile));
        } catch (Exception e) {
            LOGGER.error("Couldn't read word list from file. This PosTaggingTermFilter won't work as expected!", e);
        }
    }

    @Override
    public boolean isNamedEntityGood(Document document, NamedEntityInText entity) {
        if (entity.getLength() < MINIMUM_LENGTH_OF_NE) {
            return false;
        }
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            return false;
        }
        String surfaceForm = text.getText().substring(entity.getStartPos(), entity.getEndPos()).toLowerCase();
        return !stopwordlist.contains(surfaceForm);
    }
}
