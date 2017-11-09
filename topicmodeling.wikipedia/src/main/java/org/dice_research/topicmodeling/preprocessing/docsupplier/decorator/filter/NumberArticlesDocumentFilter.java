package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter;

import java.util.regex.Pattern;

import org.dice_research.topicmodeling.utils.doc.DocumentName;

public class NumberArticlesDocumentFilter extends AbstractDocumentPropertyBasedFilter<DocumentName> {

    private static final Pattern PATTERN = Pattern.compile("[\\d]+");

    public NumberArticlesDocumentFilter() {
        super(DocumentName.class);
    }

    public NumberArticlesDocumentFilter(boolean acceptDocumentWithoutProperty) {
        super(DocumentName.class);
    }

    @Override
    protected boolean isDocumentPropertyGood(DocumentName name) {
        return !(PATTERN.matcher(name.getName()).matches());
    }

}
