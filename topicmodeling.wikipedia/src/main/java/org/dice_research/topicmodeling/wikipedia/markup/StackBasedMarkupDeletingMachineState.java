package org.dice_research.topicmodeling.wikipedia.markup;

import org.dice_research.topicmodeling.collections.SimpleStack;

public class StackBasedMarkupDeletingMachineState implements
        MarkupDetectingAutomatonObserverState
{

    public StackBasedMarkupDeletingMachine observer;

    public String originalText;

    public StringBuilder cleanText;

    public SimpleStack<WikipediaOpeningMarkup> stack;

    public int pos;

    public StackBasedMarkupDeletingMachineState(StackBasedMarkupDeletingMachine observer, String originalText,
            StringBuilder cleanText, int pos)
    {
        this.observer = observer;
        this.originalText = originalText;
        this.cleanText = cleanText;
        this.pos = pos;
        this.stack = new SimpleStack<WikipediaOpeningMarkup>();
    }

    @Override
    public MarkupDetectingAutomatonObserver getObserver()
    {
        return observer;
    }

    public static class WikipediaOpeningMarkup
    {
        public WikipediaOpeningMarkupType type;

        public int position;

        public long pattern;

        public StringBuilder cleanedTextInsideMarkup;

        public WikipediaOpeningMarkup(WikipediaOpeningMarkupType type,
                int position, long pattern)
        {
            this.type = type;
            this.position = position;
            this.pattern = pattern;
            cleanedTextInsideMarkup = new StringBuilder();
        }

        @Override
        public String toString() {
            return "WikipediaOpeningMarkup(" + type.toString() + ", pos=" + position + ", pattern=" + pattern + ")";
        }
    }

    public static enum WikipediaOpeningMarkupType
    {
        /**
         * "{{"
         */
        MAKRO_OPENING("{{"),
        /**
         * "[["
         */
        INTERNAL_LINK_OPENING("[["),
        /**
         * "["
         */
        EXTERNAL_LINK_OPENING("["),
        /**
         * a table opened "{|" and got some data from cells
         */
        TABLE("{|"),
        /**
         * "{|"
         */
        TABLE_OPENING("{|"),
        /**
         * "\n|" or "||" or "\n!"
         */
        TABLE_CELL_START("\n|"),
        /**
         * "\n|-" or
         */
        TABLE_ROW("\n|-"),
        /**
         * "<!--"
         */
        XML_COMMENT_START("<!--"),
        /**
         * "&lt;nowiki&gt;"
         */
        NO_WIKI_TAG("<nowiki>"),
        /**
         * "&lt;pre&gt;"
         */
        PRE_TAG("<pre>"),
        /**
         * "&lt;math&gt;" or "&lt;code&gt;"
         */
        MATH_TAG("<math>"),
        /**
         * "\n"
         */
        LINEBREAK("\n"),
        /**
         * "|"
         */
        SEPARATION_BAR("|"),
        /**
         * " "
         */
        SPACE(" ");

        public final String MARKUP;

        private WikipediaOpeningMarkupType(String markup)
        {
            this.MARKUP = markup;
        }

        public int getLength()
        {
            return MARKUP.length();
        }
    }
}
