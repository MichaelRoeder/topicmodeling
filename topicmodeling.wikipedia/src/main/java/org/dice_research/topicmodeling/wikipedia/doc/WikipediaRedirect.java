package org.dice_research.topicmodeling.wikipedia.doc;

import org.dice_research.topicmodeling.utils.doc.AbstractDocumentProperty;
import org.dice_research.topicmodeling.utils.doc.ParseableDocumentProperty;

public class WikipediaRedirect extends AbstractDocumentProperty implements ParseableDocumentProperty {

    private static final long serialVersionUID = -7246753059218277351L;

    private String articleTitle;

    public WikipediaRedirect() {
    }

    public WikipediaRedirect(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    @Override
    public Object getValue() {
        return articleTitle;
    }

    @Override
    public void parseValue(String value) {
        this.articleTitle = value;
    }

}
