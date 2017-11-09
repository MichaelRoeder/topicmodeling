package org.dice_research.topicmodeling.wikipedia.doc;

import org.dice_research.topicmodeling.utils.doc.AbstractDocumentProperty;
import org.dice_research.topicmodeling.utils.doc.ParseableDocumentProperty;

public class WikipediaArticleId extends AbstractDocumentProperty implements ParseableDocumentProperty {

    private static final long serialVersionUID = -3494019987662161539L;

    private int articleId;

    public WikipediaArticleId() {
        this.articleId = -1;
    }

    public WikipediaArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public Object getValue() {
        return articleId;
    }

    @Override
    public void parseValue(String value) {
        articleId = Integer.parseInt(value);
    }

}
