package org.dice_research.topicmodeling.textmachine;

import org.apache.commons.lang3.StringEscapeUtils;

@Deprecated
public class BackupOfWikipediaMarkupDeletingMachine extends WikipediaMarkupDeletingMachine implements
        TextMachineObserver {

    /*
     * Grammar:
     * 
     * 1 "\\{\\{[^\\}]\\}\\}"
     * 
     * 2 "\\[\\[[^\\]\\|]\\]\\]"
     * 
     * 3 "\\[\\[[^\\]\\|]\\|"
     * 
     * 4 "\\]{1,2}"
     * 
     * 5 "\\[[^\\] ]\\]"
     * 
     * 6 "\\[[^\\] ] "
     * 
     * 7 "<!--"
     * 
     * 8 "-->"
     * 
     * 9 "<[^>]*>"
     * 
     * 10 "</[^>]*>"
     * 
     * 11 "[=]{2,6}"
     * 
     * 12 "[']{2,5}"
     * 
     * 13 "\\n[*#;:]+"
     * 
     * 14 "\\n\\{\\|[^\\n]*\\n"
     * 
     * 15 "[\\n\\|][\\|\\!]([^\\|\\n]*(\\{\\{[^\\}]\\}\\})?)*\\|"
     * 
     * 16 "[\\n\\|][\\|\\!]([^\\|\\n]*(\\{\\{[^\\}]\\}\\})?)*\\n"
     * 
     * 17 "\\n\\|-[^\\n]*\\n"
     * 
     * 18 "[\\n\\|][\\|\\!]([^\\|\\n]*(\\{\\{[^\\}]\\}\\})?)*\\|\\|"
     * 
     * 19 "\\n\\|\\}"
     * 
     * 20 "&[^;]*;"
     * 
     * 21 "\\}\\}"
     */

    private TextMachine textMachine;

    public BackupOfWikipediaMarkupDeletingMachine() {
        textMachine = new WikipediaMarkupDetectingMachine();
    }

    public void foundPattern(int patternId, int startPos, int endPos,
            TextMachineObserverState observerState) {
        WikipediaMarkupDeleterState state = (WikipediaMarkupDeleterState) observerState;
        // append the text between this and the last pattern
        if (startPos > state.pos) {
            state.cleanText.append(state.originalText.substring(state.pos, startPos));
        }

        switch (patternId) {
        case 2: {
            // this is a single link which should be cleaned from markups
            String linkText = state.originalText.substring(startPos + 2, endPos - 1);
            state.cleanText.append(getCleanString(linkText));
            break;
        }
        case 5: {
            // this is a single link which should be cleaned from markups
            String linkText = state.originalText.substring(startPos + 1, endPos);
            state.cleanText.append(getCleanString(linkText));
            break;
        }
        case 9:
        case 10: {
            state.cleanText.append(' ');
            break;
        }
        case 13:
        case 14:
        case 15:
        case 17: {
            state.cleanText.append('\n');
            break;
        }
        case 16: {
            // this is a single table cell which should be cleaned from markups
            String cellText = state.originalText.substring(
                    startPos + 2, endPos + 1);
            state.cleanText.append(getCleanString(cellText));
            break;
        }
        case 18: {
            // this is a single table cell which should be cleaned from markups
            String cellText = state.originalText.substring(
                    startPos + 2, endPos - 1);
            state.cleanText.append(getCleanString(cellText));
            break;
        }
        case 20: {
            state.cleanText.append(StringEscapeUtils.unescapeHtml4(state.originalText
                    .substring(startPos, endPos + 1)));
            break;
        }
        }
        state.pos = endPos + 1;
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
        WikipediaMarkupDeleterState state = new WikipediaMarkupDeleterState(this, text, new StringBuilder(), 0);
        textMachine.analyze(text, state);
        // append the rest of the text
        if (state.pos < text.length()) {
            state.cleanText.append(text.substring(state.pos));
        }

        String result = state.cleanText.toString();
        return result;
    }
}
