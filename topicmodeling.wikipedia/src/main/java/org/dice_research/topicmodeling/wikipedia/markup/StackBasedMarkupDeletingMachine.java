package org.dice_research.topicmodeling.wikipedia.markup;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dice_research.topicmodeling.collections.SimpleStack;
import org.dice_research.topicmodeling.wikipedia.markup.StackBasedMarkupDeletingMachineState.WikipediaOpeningMarkup;
import org.dice_research.topicmodeling.wikipedia.markup.StackBasedMarkupDeletingMachineState.WikipediaOpeningMarkupType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.BitSet;

public class StackBasedMarkupDeletingMachine implements
        MarkupDetectingAutomatonObserver {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(StackBasedMarkupDeletingMachine.class);

    public static final boolean REMOVE_CATEGORY_LINKS_DEFAULT = false;
    public static final boolean KEEP_TABLE_CONTENTS_DEFAULT = true;

    private static final long NORMAL_TEXT_PATTERN = 2161178;

    private static final long AFTER_MAKRO_MARKUP_PATTERN = 518;

    private static final long AFTER_NOWIKI_TAG_PATTERN = 2560;

    private static final long INSIDE_XML_COMMENT_PATTERN = 1024;

    private static final long INSIDE_WIKI_TABLE_PATTERN = 3668762;

    private static final long AFTER_TABLE_ROW_PATTERN = 3668634;

    private MarkupDetectingAutomaton automaton;
    private boolean removeCategoryLinks;
    private boolean keepTableContents;

    public StackBasedMarkupDeletingMachine() {
        this(new SimpleMarkupDetectingAutomaton(), KEEP_TABLE_CONTENTS_DEFAULT, REMOVE_CATEGORY_LINKS_DEFAULT);
    }

    public StackBasedMarkupDeletingMachine(boolean keepTableContents) {
        this(new SimpleMarkupDetectingAutomaton(), keepTableContents, REMOVE_CATEGORY_LINKS_DEFAULT);
    }

    public StackBasedMarkupDeletingMachine(boolean keepTableContents, boolean removeCategoryLinks) {
        this(new SimpleMarkupDetectingAutomaton(), keepTableContents, removeCategoryLinks);
    }

    public StackBasedMarkupDeletingMachine(MarkupDetectingAutomaton automaton, boolean keepTableContents,
            boolean removeCategoryLinks) {
        this.automaton = automaton;
        this.keepTableContents = keepTableContents;
        this.removeCategoryLinks = removeCategoryLinks;
    }

    /*
     * Grammar:
     * 
     * 0 " "
     * 
     * 1 "{{"
     * 
     * 2 "}}"
     * 
     * 3 "[["
     * 
     * 4 "["
     * 
     * 5 "]]"
     * 
     * 6 "]"
     * 
     * 7 "\n"
     * 
     * 8 "|"
     * 
     * 9 "<!--"
     * 
     * 10 "-->"
     * 
     * 11 "<[/a-zA-Z][^>]*>"
     * 
     * 12 "[=]{2,6}"
     * 
     * 13 "[']{2,3}"
     * 
     * 14 "\n[*#;:]+"
     * 
     * 15 "{|"
     * 
     * 16 "\n[\\|\\!]"
     * 
     * 17 "\n|-"
     * 
     * 18 "||"
     * 
     * 19 "\n\n[^\\|\\!]"
     * 
     * 20 "|}"
     * 
     * 21 "&[^;]*;"
     */
    @Override
    public void foundPattern(int patternId, int startPos, int endPos,
            MarkupDetectingAutomatonObserverState observerState,
            MarkupDetectingAutomatonState automatonState) {
        StackBasedMarkupDeletingMachineState state = (StackBasedMarkupDeletingMachineState) observerState;

        // If the stack is empty we can add the text between the last and the
        // current markup to the cleaned text
        // if (state.stack.size() == 0) {
        // state.cleanText.append(state.originalText.substring(state.pos,
        // startPos));
        // }
        appendText(state, startPos);

        switch (patternId) {
        case 0: { // " "
            // do not search for " " anymore, cause only the first one is interesting
            automatonState.getPatternBitSet().clear(0);
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.SPACE, startPos,
                    automatonState.getPatternBitSet().bits[0]));
            break;
        }
        case 1: { // "{{"
            // search for the "}}"
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.MAKRO_OPENING, startPos,
                    AFTER_MAKRO_MARKUP_PATTERN));
            automatonState.getPatternBitSet().bits[0] = AFTER_MAKRO_MARKUP_PATTERN;
            break;
        }
        case 2: { // "}}"
            handleClosingMarkup(WikipediaOpeningMarkupType.MAKRO_OPENING, state, automatonState);
            break;
        }
        case 3: { // "[["
            // search for the "]]"
            automatonState.getPatternBitSet().set(5);
            // search for "|"
            automatonState.getPatternBitSet().set(8);
            // search for "\n"
            automatonState.getPatternBitSet().set(7);
            // do not search for "]"
            automatonState.getPatternBitSet().clear(6);
            // do not search for " "
            automatonState.getPatternBitSet().clear(0);
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.INTERNAL_LINK_OPENING, startPos,
                    automatonState.getPatternBitSet().bits[0]));
            break;
        }
        case 4: { // "["
            // search for the "]"
            automatonState.getPatternBitSet().set(6);
            // search for " "
            automatonState.getPatternBitSet().set(0);
            // search for "\n"
            automatonState.getPatternBitSet().set(7);
            // do not search for another "["
            automatonState.getPatternBitSet().clear(4);
            // do not search for "]]"
            automatonState.getPatternBitSet().clear(5);
            // do not search for "|"
            automatonState.getPatternBitSet().clear(8);
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.EXTERNAL_LINK_OPENING, startPos,
                    automatonState.getPatternBitSet().bits[0]));
            break;
        }
        case 5: { // "]]"
            handleClosingMarkup(WikipediaOpeningMarkupType.INTERNAL_LINK_OPENING, state, automatonState);
            break;
        }
        case 6: { // "]"
            handleClosingMarkup(WikipediaOpeningMarkupType.EXTERNAL_LINK_OPENING, state, automatonState);
            break;
        }
        case 7: { // "\n"
            handleClosingMarkup(WikipediaOpeningMarkupType.LINEBREAK, state, automatonState);
            appendText("\n", state);
            break;
        }
        case 8: { // "|"
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.SEPARATION_BAR, startPos,
                    automatonState.getPatternBitSet().bits[0]));
            break;
        }
        case 9: { // "<!--"
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.XML_COMMENT_START, startPos,
                    INSIDE_XML_COMMENT_PATTERN));
            automatonState.getPatternBitSet().bits[0] = INSIDE_XML_COMMENT_PATTERN;
            break;
        }
        case 10: { // "-->"
            handleClosingMarkup(WikipediaOpeningMarkupType.XML_COMMENT_START, state, automatonState);
            break;
        }
        case 11: { // "<[^>]*>"
            if ((endPos - startPos) > 1) {
                handleTag(state, automatonState, startPos, endPos);
            }
            break;
        }
        case 12: { // "[=]{2,6}"
            // nothing to do
            break;
        }
        case 13: { // "[']{2,3}"
            appendText(" ", state);
            break;
        }
        case 14: { // "\n[*#;:]+"
            // if the machine is also searching for a LINEBREAK
            if (automatonState.getPatternBitSet().get(7)) {
                handleClosingMarkup(WikipediaOpeningMarkupType.LINEBREAK, state, automatonState);
            }
            appendText("\n", state);
            break;
        }
        case 15: { // "{|"
            // If there is a table row on the stack
            if ((state.stack.size() > 0) && (state.stack.get().type == WikipediaOpeningMarkupType.TABLE_ROW)) {
                // close the row
                handleClosingMarkup(WikipediaOpeningMarkupType.LINEBREAK, state, automatonState);
            }

            // // search for "|"
            // automatonState.getPatternBitSet().set(8);
            // // search for "\n[\\|\\!]"
            // automatonState.getPatternBitSet().set(16);
            // // search for "\n|-"
            // automatonState.getPatternBitSet().set(17);
            // // search for "||"
            // automatonState.getPatternBitSet().set(18);
            // // // search for "\n\n"
            // // automatonState.getPatternBitSet().set(19);
            // // search for "|}"
            // automatonState.getPatternBitSet().set(20);
            // // automatonState.getPatternBitSet().set(16, 21);
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.TABLE, startPos,
                    INSIDE_WIKI_TABLE_PATTERN));
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.TABLE_OPENING, startPos,
                    INSIDE_WIKI_TABLE_PATTERN));
            automatonState.getPatternBitSet().bits[0] = INSIDE_WIKI_TABLE_PATTERN;
            break;
        }
        case 16: { // "\n[\\|\\!]"
            handleClosingMarkup(WikipediaOpeningMarkupType.TABLE_CELL_START, state, automatonState);
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.TABLE_CELL_START, startPos,
                    automatonState.getPatternBitSet().bits[0]));
            break;
        }
        case 17: { // "\n|-"
            handleClosingMarkup(WikipediaOpeningMarkupType.TABLE_CELL_START, state, automatonState);
            // // search for "\n"
            // automatonState.getPatternBitSet().set(7);
            // // do not search for "|"
            // automatonState.getPatternBitSet().clear(8);
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.TABLE_ROW, startPos,
                    AFTER_TABLE_ROW_PATTERN));
            automatonState.getPatternBitSet().bits[0] = AFTER_TABLE_ROW_PATTERN;
            // this is a new line in a table
            break;
        }
        case 18: { // "||"
            handleClosingMarkup(WikipediaOpeningMarkupType.TABLE_CELL_START, state, automatonState);
            state.stack.add(new WikipediaOpeningMarkup(
                    WikipediaOpeningMarkupType.TABLE_CELL_START, startPos,
                    automatonState.getPatternBitSet().bits[0]));
            break;
        }
        case 20: { // "|}"
            handleClosingMarkup(WikipediaOpeningMarkupType.TABLE, state, automatonState);
            break;
        }
        case 21: { // "&[^;]*;"
            // unescape this char
            appendText(StringEscapeUtils.unescapeHtml4(state.originalText
                    .substring(startPos, endPos + 1)), state);
            break;
        }
        } // switch(patternId)
        state.pos = endPos + 1;
    }

    private boolean isOpeningMarkupOnStack(SimpleStack<WikipediaOpeningMarkup> stack,
            WikipediaOpeningMarkupType type) {
        for (int i = 0; i < stack.size(); ++i) {
            if (stack.get(i).type == type) {
                return true;
            }
        }
        return false;
    }

    private void appendText(StackBasedMarkupDeletingMachineState state, int startPos) {
        if (state.pos < startPos) {
            appendText(state.originalText.substring(state.pos, startPos), state);
        }
    }

    private void appendText(CharSequence text, StackBasedMarkupDeletingMachineState state) {
        if (state.stack.size() > 0) {
            WikipediaOpeningMarkup openingMarkup = state.stack.get();
            openingMarkup.cleanedTextInsideMarkup.append(text);
        } else {
            state.cleanText.append(text);
        }
    }

    private void handleClosingMarkup(WikipediaOpeningMarkupType type, StackBasedMarkupDeletingMachineState state,
            MarkupDetectingAutomatonState automatonState) {
        // if a markup has been removed from the stack
        if (handleClosingMarkup(type, state)) {
            // update the pattern of the automaton
            setPatternBitSet(automatonState.getPatternBitSet(), state.stack);
        }
    }

    private boolean handleClosingMarkup(WikipediaOpeningMarkupType type, StackBasedMarkupDeletingMachineState state) {
        StringBuilder cleanedTextInsideMarkup = null;
        boolean removeMarkupFromStack = true;
        switch (type) {
        case INTERNAL_LINK_OPENING: {
            if (state.stack.size() > 0) {
                cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
                // If category links should be removed and this is a category link
                if ((removeCategoryLinks) && (cleanedTextInsideMarkup.toString().toLowerCase().contains("category:"))) {
                    cleanedTextInsideMarkup = null;
                }
            }
            removeIntermediateMarkupsFromStack(WikipediaOpeningMarkupType.SEPARATION_BAR, state, false);
            break;
        }
        case EXTERNAL_LINK_OPENING: {
            if (state.stack.size() > 0) {
                cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
            }
            removeIntermediateMarkupsFromStack(WikipediaOpeningMarkupType.SPACE, state, false);
            break;
        }
        case LINEBREAK: {
            // check what lies on the stack
            WikipediaOpeningMarkup nextClosableMarkup = getNextClosableMarkup(state.stack);
            WikipediaOpeningMarkup removedMarkup = null;
            // there is a
            if ((nextClosableMarkup != null) && (nextClosableMarkup.type == WikipediaOpeningMarkupType.TABLE_ROW)) {
                type = WikipediaOpeningMarkupType.TABLE_ROW;
            } else {
                // check whether there is an internal or an external Link on the stack
                while ((nextClosableMarkup != null)
                        && ((nextClosableMarkup.type == WikipediaOpeningMarkupType.INTERNAL_LINK_OPENING) || (nextClosableMarkup.type == WikipediaOpeningMarkupType.EXTERNAL_LINK_OPENING))) {
                    // Go through the stack until the known next closable markup is found
                    while (removedMarkup != nextClosableMarkup) {
                        // remove the next markup
                        removedMarkup = state.stack.getAndRemove();
                        // append the SPACE or the SEPARATION_BAR (which seems to be part of the text)
                        appendText(removedMarkup.type.MARKUP, state);
                        appendText(removedMarkup.cleanedTextInsideMarkup, state);
                    }
                    // get the next closable markup and make sure that it can't be closed by this linebreak
                    nextClosableMarkup = getNextClosableMarkup(state.stack);
                }
                if (removedMarkup != null) {
                    // append the last removed markup so it can be removed at the end of this method
                    state.stack.add(removedMarkup);
                    type = removedMarkup.type;
                } else {
                    LOGGER.error("Found a LINEBREAK while there is no markup on the stack which could be closed by it.(nextClosableMarkup="
                            + nextClosableMarkup + ")");
                }
            }
            break;
        }
        case TABLE: {
            // close the last cell (If there is no cell nothing will happen)
            handleClosingMarkup(WikipediaOpeningMarkupType.TABLE_CELL_START, state);
            // make sure that all tablerelated markups are closed
            WikipediaOpeningMarkup nextClosableMarkup = getNextClosableMarkup(state.stack);
            if (nextClosableMarkup != null) {
                boolean isTableRelated = true;
                do {
                    nextClosableMarkup = getNextClosableMarkup(state.stack);
                    isTableRelated = (nextClosableMarkup.type == WikipediaOpeningMarkupType.TABLE_OPENING)
                            || (nextClosableMarkup.type == WikipediaOpeningMarkupType.TABLE_ROW)
                            || (nextClosableMarkup.type == WikipediaOpeningMarkupType.TABLE_CELL_START);
                    if (isTableRelated) {
                        handleClosingMarkup(WikipediaOpeningMarkupType.TABLE_CELL_START, state);
                    }
                } while ((nextClosableMarkup != null)
                        && isTableRelated);
                // close the table by saving its content
                if ((state.stack.size() > 0) && (keepTableContents)) {
                    cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
                }
            }
            // close the table by saving its content
            if ((state.stack.size() > 0) && (keepTableContents)) {
                cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
            }
            break;
        }
        case TABLE_OPENING: {
            // close the last cell
            // handleClosingMarkup(WikipediaOpeningMarkupType.TABLE_CELL_START, state);
            // close the table by saving its content
            // if (state.stack.size() > 0) {
            // cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
            // }
            break;
        }
        case TABLE_CELL_START: {
            WikipediaOpeningMarkup nextClosableMarkup = null;
            boolean notTableCellRelated = true;
            // make sure that there is a closable markup which can be closed by this table cell end
            do {
                nextClosableMarkup = getNextClosableMarkup(state.stack);
                notTableCellRelated = (nextClosableMarkup.type != WikipediaOpeningMarkupType.TABLE_OPENING)
                        && (nextClosableMarkup.type != WikipediaOpeningMarkupType.TABLE_ROW)
                        && (nextClosableMarkup.type != WikipediaOpeningMarkupType.TABLE_CELL_START)
                        && (nextClosableMarkup.type != WikipediaOpeningMarkupType.TABLE);
                if (notTableCellRelated) {
                    // there is an unclosed markup which started inside the cell. Close this markup before closing the
                    // cell.
                    handleClosingMarkup(nextClosableMarkup.type, state);
                }
            } while ((nextClosableMarkup != null)
                    && notTableCellRelated);
            if (nextClosableMarkup != null) {
                cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
                removeIntermediateMarkupsFromStack(WikipediaOpeningMarkupType.SEPARATION_BAR, state, false);
                switch (nextClosableMarkup.type) {
                case TABLE_OPENING: {
                    cleanedTextInsideMarkup = nextClosableMarkup.cleanedTextInsideMarkup;
                    cleanedTextInsideMarkup.delete(0, cleanedTextInsideMarkup.length());
                    cleanedTextInsideMarkup.append('\n');
                    type = WikipediaOpeningMarkupType.TABLE_OPENING;
                    break;
                }
                case TABLE_ROW: {
                    cleanedTextInsideMarkup = null;
                    type = WikipediaOpeningMarkupType.TABLE_ROW;
                    break;
                }
                case TABLE_CELL_START: {
                    cleanedTextInsideMarkup.append('\n');
                    break;
                }
                case TABLE: {
                    removeMarkupFromStack = false;
                    cleanedTextInsideMarkup = null;
                    break;
                }
                default:
                    // nothing to do for default
                    break;
                }
            }
            break;
        }
        case PRE_TAG: {
            removeIntermediateMarkupsFromStack(state, true,
                    Arrays.asList(WikipediaOpeningMarkupType.NO_WIKI_TAG, WikipediaOpeningMarkupType.MATH_TAG));
            if (state.stack.size() > 0) {
                cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
            }
            break;
        }
        case NO_WIKI_TAG: {
            removeIntermediateMarkupsFromStack(state, true,
                    Arrays.asList(WikipediaOpeningMarkupType.PRE_TAG, WikipediaOpeningMarkupType.MATH_TAG));
            if (state.stack.size() > 0) {
                cleanedTextInsideMarkup = state.stack.get().cleanedTextInsideMarkup;
            }
            break;
        }
        case MATH_TAG: {
            removeIntermediateMarkupsFromStack(state, false,
                    Arrays.asList(WikipediaOpeningMarkupType.NO_WIKI_TAG, WikipediaOpeningMarkupType.PRE_TAG));
        }
        case MAKRO_OPENING:
        case XML_COMMENT_START: {
            // nothing special to do
            break;
        }
        default: {
            throw new IllegalArgumentException("Got illegal type " + type);
        }
        }
        if (removeMarkupFromStack) {
            removeFromStack(type, state.stack);
        }
        if (cleanedTextInsideMarkup != null) {
            appendText(cleanedTextInsideMarkup, state);
        }
        return removeMarkupFromStack;
    }

    private WikipediaOpeningMarkup getNextClosableMarkup(SimpleStack<WikipediaOpeningMarkup> stack) {
        Iterator<WikipediaOpeningMarkup> iterator = stack.iterator();
        while (iterator.hasNext()) {
            WikipediaOpeningMarkup markup = iterator.next();
            if ((markup.type != WikipediaOpeningMarkupType.SEPARATION_BAR)
                    && (markup.type != WikipediaOpeningMarkupType.SPACE)) {
                return markup;
            }
        }
        return null;
    }

    private void removeFromStack(WikipediaOpeningMarkupType type,
            SimpleStack<WikipediaOpeningMarkup> stack) {
        if (stack.size() > 0) {
            WikipediaOpeningMarkup openingMarkup = stack
                    .getAndRemove();
            if (openingMarkup.type != type) {
                LOGGER.error("Could not remove state from stack! On the stack was a "
                        + openingMarkup.type.toString()
                        + " instead of the expected "
                        + type.toString()
                        + "!");
            }
        } else {
            LOGGER.error("Could not remove state from stack! The stack is empty while it should contain the expected "
                    + type.toString() + "!");
        }
    }

    private void removeIntermediateMarkupsFromStack(WikipediaOpeningMarkupType intermediateType,
            StackBasedMarkupDeletingMachineState state, boolean appendTheirText) {
        if (state.stack.size() > 0) {
            WikipediaOpeningMarkup currentMarkup = state.stack.get();
            while ((currentMarkup != null)
                    && (currentMarkup.type == intermediateType)) {
                state.stack.remove();
                if (appendTheirText) {
                    appendText(currentMarkup.cleanedTextInsideMarkup, state);
                }
                currentMarkup = (state.stack.size() > 0) ? state.stack.get() : null;
            }
        }
    }

    private void removeIntermediateMarkupsFromStack(StackBasedMarkupDeletingMachineState state,
            boolean appendTheirText, List<WikipediaOpeningMarkupType> intermediateType) {
        if (state.stack.size() > 0) {
            WikipediaOpeningMarkup currentMarkup = state.stack.get();
            while ((currentMarkup != null)
                    && (intermediateType.contains(currentMarkup.type))) {
                state.stack.remove();
                if (appendTheirText) {
                    appendText(currentMarkup.cleanedTextInsideMarkup, state);
                }
                currentMarkup = (state.stack.size() > 0) ? state.stack.get() : null;
            }
        }
    }

    private void setPatternBitSet(BitSet patternBitSet,
            SimpleStack<WikipediaOpeningMarkup> stack) {
        if (stack.size() > 0) {
            patternBitSet.bits[0] = stack.get().pattern;
        }
        else {
            patternBitSet.bits[0] = NORMAL_TEXT_PATTERN;
        }
    }

    public String getCleanText(String text) {
        // we put a "\n" at the beginning of the text because some of the patterns which should also be able to match
        // the start of the text are starting with a "\n".
        if (text.startsWith("#")) {
            String temp = text.substring(0,
                    text.length() < 14 ? text.length() : 14);
            temp = temp.toLowerCase();
            if (temp.startsWith("#redirect")) {
                return getCleanString("\n" + text.substring(9));
            } else if (temp.startsWith("#weiterleitung")) {
                return getCleanString("\n" + text.substring(14));
            }
        }
        return getCleanString("\n" + text);
    }

    private String getCleanString(String text) {
        BitSet patternBitSet = new BitSet(22);
        patternBitSet.bits[0] = NORMAL_TEXT_PATTERN;
        StackBasedMarkupDeletingMachineState state = new StackBasedMarkupDeletingMachineState(this, text,
                new StringBuilder(), 0);
        automaton.analyze(text, state, patternBitSet);
        cleanupStack(state);
        if (state.pos < text.length()) {
            state.cleanText.append(text.substring(state.pos));
        }
        return state.cleanText.toString();
    }

    private void cleanupStack(StackBasedMarkupDeletingMachineState state) {
        if (state.stack.size() > 0) {
            LOGGER.info("There are unclosed tags.(" + state.stack.toString() + "). Try to close them.");
            WikipediaOpeningMarkup nextClosableMarkup = getNextClosableMarkup(state.stack);
            while (nextClosableMarkup != null) {
                closeNextClosableMarkup(state, nextClosableMarkup);
                nextClosableMarkup = getNextClosableMarkup(state.stack);
            }
        }
    }

    @SuppressWarnings("unused")
    private void closeNextClosableMarkup(StackBasedMarkupDeletingMachineState state) {
        WikipediaOpeningMarkup nextClosableMarkup = getNextClosableMarkup(state.stack);
        if (nextClosableMarkup != null) {
            closeNextClosableMarkup(state, nextClosableMarkup);
        }
    }

    private void closeNextClosableMarkup(StackBasedMarkupDeletingMachineState state,
            WikipediaOpeningMarkup nextClosableMarkup) {
        if ((nextClosableMarkup.type == WikipediaOpeningMarkupType.TABLE_CELL_START)
                || (nextClosableMarkup.type == WikipediaOpeningMarkupType.TABLE_ROW)
                || (nextClosableMarkup.type == WikipediaOpeningMarkupType.TABLE_OPENING)) {
            // close the whole table
            handleClosingMarkup(WikipediaOpeningMarkupType.TABLE, state);
        } else {
            handleClosingMarkup(nextClosableMarkup.type, state);
        }
    }

    private void handleTag(StackBasedMarkupDeletingMachineState state,
            MarkupDetectingAutomatonState automatonState, int startPos, int endPos) {
        // get the tag name
        String tagString = state.originalText.substring(startPos + 1, endPos).toLowerCase();
        if (tagString.endsWith("/")) {
            // this is an empty tag
            return;
        }
        int pos = tagString.indexOf(' ');
        String tagName = (pos >= 0) ? tagString.substring(0, pos) : tagString;

        WikipediaOpeningMarkupType tagTypeToAdd = null;
        WikipediaOpeningMarkupType tagTypeOnStack;
        if (tagName.equals("nowiki")) {
            if (state.stack.size() > 0) {
                tagTypeOnStack = state.stack.get().type;
                if (tagTypeOnStack != WikipediaOpeningMarkupType.NO_WIKI_TAG) {
                    tagTypeToAdd = WikipediaOpeningMarkupType.NO_WIKI_TAG;
                }
            } else {
                tagTypeToAdd = WikipediaOpeningMarkupType.NO_WIKI_TAG;
            }
        } else if (tagName.equals("pre")) {
            if (state.stack.size() > 0) {
                tagTypeOnStack = state.stack.get().type;
                if (tagTypeOnStack != WikipediaOpeningMarkupType.PRE_TAG) {
                    tagTypeToAdd = WikipediaOpeningMarkupType.PRE_TAG;
                }
            } else {
                tagTypeToAdd = WikipediaOpeningMarkupType.PRE_TAG;
            }
        } else if (tagName.startsWith("math") || tagName.startsWith("code")) {
            if (state.stack.size() > 0) {
                tagTypeOnStack = state.stack.get().type;
                if ((tagTypeOnStack != WikipediaOpeningMarkupType.NO_WIKI_TAG)
                        && (tagTypeOnStack != WikipediaOpeningMarkupType.PRE_TAG)
                        && (tagTypeOnStack != WikipediaOpeningMarkupType.MATH_TAG)) {
                    tagTypeToAdd = WikipediaOpeningMarkupType.MATH_TAG;
                }
            } else {
                tagTypeToAdd = WikipediaOpeningMarkupType.MATH_TAG;
            }
        } else if (tagName.equals("/nowiki")) {
            // nowiki end tag has been found
            if (isOpeningMarkupOnStack(state.stack, WikipediaOpeningMarkupType.NO_WIKI_TAG)) {
                handleClosingMarkup(WikipediaOpeningMarkupType.NO_WIKI_TAG, state, automatonState);
            }
        } else if (tagName.equals("/pre")) {
            // pre end tag has been found
            if (isOpeningMarkupOnStack(state.stack, WikipediaOpeningMarkupType.PRE_TAG)) {
                handleClosingMarkup(WikipediaOpeningMarkupType.PRE_TAG, state, automatonState);
            }
        } else if (tagName.equals("/math") || tagName.equals("/code")) {
            // pre end tag has been found
            if (isOpeningMarkupOnStack(state.stack, WikipediaOpeningMarkupType.MATH_TAG)) {
                handleClosingMarkup(WikipediaOpeningMarkupType.MATH_TAG, state, automatonState);
            }
        }

        if (tagTypeToAdd != null) {
            state.stack.add(new WikipediaOpeningMarkup(tagTypeToAdd, startPos,
                    AFTER_NOWIKI_TAG_PATTERN));
            automatonState.getPatternBitSet().bits[0] = AFTER_NOWIKI_TAG_PATTERN;
        }
    }

    public static void main(String[] args) {
        // Grammar:
        // 0 " "
        // 1 "{{"
        // 2 "}}"
        // 3 "[["
        // 4 "["
        // 5 "]]"
        // 6 "]"
        // 7 "\n"
        // 8 "|"
        // 9 "<!--"
        // 10 "-->"
        // 11 "<[/a-zA-Z][^>]*>"
        // 12 "[=]{2,6}"
        // 13 "[']{2,3}"
        // 14 "\n[*#;:]+"
        // 15 "{|"
        // 16 "\n[\\|\\!]"
        // 17 "\n|-"
        // 18 "||"
        // 19 "\n\n[^\\|\\!]"
        // 20 "|}"
        // 21 "&[^;]*;"

        System.out
                .println("This method prints out the long values for the pattern bit set");
        BitSet patternBitSet = new BitSet(22);
        patternBitSet.set(1);
        patternBitSet.set(3);
        patternBitSet.set(4);
        patternBitSet.set(9);
        patternBitSet.set(11);
        patternBitSet.set(12);
        patternBitSet.set(13);
        patternBitSet.set(14);
        patternBitSet.set(15);
        patternBitSet.set(21);
        System.out.println("pattern for normal text: "
                + Long.toBinaryString(patternBitSet.bits[0]) + " = "
                + patternBitSet.bits[0]);

        long normalTextPattern = patternBitSet.bits[0];

        patternBitSet.clear();
        patternBitSet.bits[0] = normalTextPattern;
        // search for "|"
        patternBitSet.set(8);
        // search for "\n[\\|\\!]"
        patternBitSet.set(16);
        // search for "\n|-"
        patternBitSet.set(17);
        // search for "||"
        patternBitSet.set(18);
        // search for "|}"
        patternBitSet.set(20);
        System.out.println("pattern for wikitables and table cells text: "
                + Long.toBinaryString(patternBitSet.bits[0]) + " = "
                + patternBitSet.bits[0]);

        // search for "\n"
        patternBitSet.set(7);
        // do not search for "|"
        patternBitSet.clear(8);
        System.out.println("pattern for table rows text: "
                + Long.toBinaryString(patternBitSet.bits[0]) + " = "
                + patternBitSet.bits[0]);

        patternBitSet.clear();
        patternBitSet.set(1);
        patternBitSet.set(2);
        patternBitSet.set(9);
        System.out.println("pattern after makro start markup: "
                + Long.toBinaryString(patternBitSet.bits[0]) + " = "
                + patternBitSet.bits[0]);
        patternBitSet.clear();
        patternBitSet.set(11);
        patternBitSet.set(9);
        System.out.println("pattern after nowiki tag: "
                + Long.toBinaryString(patternBitSet.bits[0]) + " = "
                + patternBitSet.bits[0]);
        patternBitSet.clear();
        patternBitSet.set(10);
        System.out.println("pattern inside XML comment: "
                + Long.toBinaryString(patternBitSet.bits[0]) + " = "
                + patternBitSet.bits[0]);
    }

    public void setRemoveCategoryLinks(boolean removeCategoryLinks) {
        this.removeCategoryLinks = removeCategoryLinks;
    }

    public void setKeepTableContents(boolean keepTableContents) {
        this.keepTableContents = keepTableContents;
    }
}
