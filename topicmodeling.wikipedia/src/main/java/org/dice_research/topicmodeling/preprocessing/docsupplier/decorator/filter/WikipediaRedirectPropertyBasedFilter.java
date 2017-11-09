package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter.AbstractDocumentPropertyBasedFilter;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaRedirect;

public class WikipediaRedirectPropertyBasedFilter extends AbstractDocumentPropertyBasedFilter<WikipediaRedirect> {

    public WikipediaRedirectPropertyBasedFilter() {
        super(WikipediaRedirect.class, true);
    }

    @Override
    protected boolean isDocumentPropertyGood(WikipediaRedirect property) {
        // if there is no title
        return ((property.getArticleTitle() == null) || (property.getArticleTitle().isEmpty()));
    }
}
