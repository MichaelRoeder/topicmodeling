package org.dice_research.topicmodeling.wikipedia.doc;

import org.dice_research.topicmodeling.utils.doc.AbstractDocumentProperty;
import org.dice_research.topicmodeling.utils.doc.ParseableDocumentProperty;

public class WikipediaNamespace extends AbstractDocumentProperty implements ParseableDocumentProperty {

    private static final long serialVersionUID = -5177044646640925923L;

    private int namespaceId;

    public WikipediaNamespace() {
        this.namespaceId = 0;
    }

    public WikipediaNamespace(int namespaceId) {
        this.namespaceId = namespaceId;
    }

    public int getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(int namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return "WikipediaNamespace=" + namespaceId + "(=\"" + getNamespaceTitle(namespaceId) + "\")";
    }

    @Override
    public Object getValue() {
        return namespaceId;
    }

    @Override
    public void parseValue(String value) {
        namespaceId = Integer.parseInt(value);
    }

    public static String getNamespaceTitle(int namespaceId) {
        switch (namespaceId) {
        case -2: {
            return "Media";
        }
        case -1: {
            return "Special";
        }
        case 0: {
            return "NO NAMESPACE";
        }
        case 1: {
            return "Talk";
        }
        case 2: {
            return "User";
        }
        case 3: {
            return "User talk";
        }
        case 4: {
            return "Wikipedia";
        }
        case 5: {
            return "Wikipedia talk";
        }
        case 6: {
            return "File";
        }
        case 7: {
            return "File talk";
        }
        case 8: {
            return "MediaWiki";
        }
        case 9: {
            return "MediaWiki talk";
        }
        case 10: {
            return "Template";
        }
        case 11: {
            return "Template talk";
        }
        case 12: {
            return "Help";
        }
        case 13: {
            return "Help talk";
        }
        case 14: {
            return "Category";
        }
        case 15: {
            return "Category talk";
        }
        case 100: {
            return "Portal";
        }
        case 101: {
            return "Portal talk";
        }
        case 108: {
            return "Book";
        }
        case 109: {
            return "Book talk";
        }
        case 446: {
            return "Education Program";
        }
        case 447: {
            return "Education Program talk";
        }
        case 710: {
            return "TimedText";
        }
        case 711: {
            return "TimedText talk";
        }
        case 828: {
            return "Module";
        }
        case 829: {
            return "Module talk";
        }
        default: {
            return "UNKNOWN NAMESPACE";
        }
        }
    }
}
