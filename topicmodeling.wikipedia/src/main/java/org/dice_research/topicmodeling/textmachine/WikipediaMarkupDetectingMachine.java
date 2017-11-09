package org.dice_research.topicmodeling.textmachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikipediaMarkupDetectingMachine implements TextMachine {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(WikipediaMarkupDetectingMachine.class);

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
     * 
     * 22 "\\]\\]" and saw 3 before
     * 
     * 23 "\\|" and saw 3 before
     * 
     * 24 "\\]" and saw 6 before
     */

    @Override
    public void analyze(String text, TextMachineObserverState observerState) {
        char[] chars = text.toCharArray();
        char c;
        int state = 0;
        int startPos = 0;
        for (int pos = 0; pos < chars.length; ++pos) {
            c = chars[pos];
            switch (state) {
            case 0: {
                switch (c) {
                case '{': {
                    state = 1;
                    startPos = pos;
                    break;
                }
                case '<': {
                    state = 4;
                    startPos = pos;
                    break;
                }
                case '[': {
                    state = 9;
                    startPos = pos;
                    break;
                }
                case ']': {
                    state = 26;
                    startPos = pos;
                    break;
                }
                case '=': {
                    state = 13;
                    startPos = pos;
                    break;
                }
                case '\'': {
                    state = 15;
                    startPos = pos;
                    break;
                }
                case '-': {
                    state = 17;
                    startPos = pos;
                    break;
                }
                case '\n': {
                    state = 19;
                    startPos = pos;
                    break;
                }
                case '&': {
                    state = 27;
                    startPos = pos;
                    break;
                }
                case '}': {
                    state = 30;
                    startPos = pos;
                    break;
                }
                case '|': {
                    state = 31;
                    startPos = pos;
                    break;
                }
                default: {
                    // nothing to do for default
                    break;
                }
                }
                break;
            }
            case 1: { // saw "\\{" before
                if (c == '{') {
                    state = 2;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 2: { // saw "\\{\\{" before
                if (c == '}') {
                    state = 3;
                }
                // nothing to do for else
                break;
            }
            case 3: { // saw "\\{\\{[^\\}]*\\}\\}*" before
                if (c != '}') {
                    observerState.getObserver().foundPattern(1, startPos, pos - 1, observerState);
                    state = 0;
                    --pos;
                }
                // nothing to do for else
                break;
            }
            case 4: { // saw "<" before
                switch (c) {
                case '!': {
                    state = 5;
                    break;
                }
                case '/': {
                    state = 7;
                    break;
                }
                default: {
                    state = 8;
                    break;
                }
                }
                break;
            }
            case 5: { // saw "<!" before
                switch (c) {
                case '-': {
                    state = 5;
                    break;
                }
                case '>': {
                    observerState.getObserver().foundPattern(9, startPos, pos, observerState);
                    state = 0;
                    break;
                }
                default: {
                    state = 8;
                    break;
                }
                }
                break;
            }
            case 6: { // saw "<!-" before
                switch (c) {
                case '-': {
                    observerState.getObserver().foundPattern(7, startPos, pos, observerState);
                    state = 0;
                    break;
                }
                case '>': {
                    observerState.getObserver().foundPattern(9, startPos, pos, observerState);
                    state = 0;
                    break;
                }
                default: {
                    state = 8;
                    break;
                }
                }
                break;
            }
            case 7: { // saw "</[^>]*" before
                if (c == '>') {
                    observerState.getObserver().foundPattern(9, startPos, pos, observerState);
                    state = 0;
                }
                // nothing to do for else
                break;
            }
            case 8: { // saw "<[^>]*" before
                if (c == '>') {
                    observerState.getObserver().foundPattern(9, startPos, pos, observerState);
                    state = 0;
                }
                break;
            }
            case 9: { // saw "[" before
                if (c == '[') {
                    state = 10;
                } else {
                    state = 12;
                }
                break;
            }
            case 10: { // saw "\\[\\[[^\\]]*" before
                switch (c) {
                case ']': {
                    state = 11;
                    break;
                }
                case '|': {
                    observerState.getObserver().foundPattern(3, startPos, pos, observerState);
                    startPos = pos + 1;
                    state = 37;
                    break;
                }
                default: {
                    // nothing to do for default
                    break;
                }
                }
                break;
            }
            case 11: { // saw "\\[\\[[^\\]]*\\]" before
                if (c != ']') {
                    LOGGER.info("There seems to be an error in the wiki markup. Found a link starting with \"[[\" but ending with \"]"
                            + c + "\".");
                    --pos;
                }
                observerState.getObserver().foundPattern(2, startPos, pos, observerState);
                state = 0;
                break;
            }
            case 12: { // saw "\\[[^\\[\\]\\|][^\\]\\|]*" before
                switch (c) {
                case ']': {
                    observerState.getObserver().foundPattern(5, startPos, pos, observerState);
                    state = 0;
                    break;
                }
                case ' ': {
                    observerState.getObserver().foundPattern(6, startPos, pos, observerState);
                    state = 39;
                    startPos = pos + 1;
                    break;
                }
                default: {
                    // nothing to do for default
                    break;
                }
                }
                break;
            }
            case 13: { // saw "=" before
                if (c == '=') {
                    state = 14;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 14: { // saw "==*" before
                if (c == '=') {
                    if ((pos - startPos) > 6) {
                        observerState.getObserver().foundPattern(11, startPos, pos - 1, observerState);
                        state = 0;
                        --pos;
                    }
                    break;
                } else {
                    observerState.getObserver().foundPattern(11, startPos, pos - 1, observerState);
                    state = 0;
                    --pos;
                    break;
                }
            }
            case 15: { // saw "'" before
                if (c == '\'') {
                    state = 16;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 16: { // saw "''*" before
                if (c == '\'') {
                    if ((pos - startPos) > 5) {
                        observerState.getObserver().foundPattern(12, startPos, pos - 1, observerState);
                        state = 0;
                        --pos;
                    }
                    break;
                } else {
                    observerState.getObserver().foundPattern(12, startPos, pos - 1, observerState);
                    state = 0;
                    --pos;
                    break;
                }
            }
            case 17: { // saw "-" before
                if (c == '-') {
                    state = 18;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 18: { // saw "--" before
                if (c == '>') {
                    observerState.getObserver().foundPattern(8, startPos, pos - 1, observerState);
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 19: { // saw "\n" before
                switch (c) {
                case '*': // falls through
                case '#': // falls through
                case ';': // falls through
                case ':': {
                    state = 20;
                    break;
                }
                case '{': {
                    state = 21;
                    break;
                }
                case '!': {
                    state = 23;
                    break;
                }
                case '|': {
                    state = 24;
                    break;
                }
                case ' ': {
                    // nothing to do
                    break;
                }
                default: {
                    state = 0;
                    --pos;
                    break;
                }
                }
                break;
            }
            case 20: { // saw "\n[*#;:]" before
                if ((c != '*') && (c != '#') && (c != ';') && (c != ':')) {
                    observerState.getObserver().foundPattern(13, startPos, pos - 1, observerState);
                    state = 0;
                    --pos;
                }
                break;
            }
            case 21: { // saw "\\\n\\{" before
                switch (c) {
                case '{': {
                    state = 2;
                    ++startPos;
                    break;
                }
                case '|': {
                    state = 22;
                    break;
                }
                default: {
                    state = 0;
                    --pos;
                    break;
                }
                }
                break;
            }
            case 22: { // saw "\\\n\\{\\|[^\\\n]*" before
                if (c == '\n') {
                    observerState.getObserver().foundPattern(14, startPos, pos, observerState);
                    state = 19;
                    startPos = pos;
                }
                // nothing to do for else
                break;
            }
            case 23: { // saw "\\\n[!\\|][^\\\n\\|]" before
                switch (c) {
                case '|': {
                    // observerState.getObserver().foundPattern(15, startPos, pos, observerState);
                    state = 32;
                    break;
                }
                case '\n': {
                    observerState.getObserver().foundPattern(16, startPos, pos, observerState);
                    state = 19;
                    startPos = pos;
                    break;
                }
                case '{': {
                    state = 33;
                    break;
                }
                case '[': {
                    state = 35;
                    break;
                }
                default: {
                    // nothing to do for default
                    break;
                }
                }
                break;
            }
            case 24: { // saw "\\\n\\|" before
                switch (c) {
                case '-': {
                    state = 25;
                    break;
                }
                case '}': {
                    observerState.getObserver().foundPattern(19, startPos, pos, observerState);
                    state = 0;
                    break;
                }
                case '\n': {
                    observerState.getObserver().foundPattern(16, startPos, pos, observerState);
                    state = 19;
                    startPos = pos;
                    break;
                }
                default: {
                    state = 23;
                    break;
                }
                }
                break;
            }
            case 25: { // saw "\\\n\\{\\|-[^\\\n]*" before
                if (c == '\n') {
                    observerState.getObserver().foundPattern(17, startPos, pos, observerState);
                    state = 19;
                    startPos = pos;
                }
                // nothing to do for else
                break;
            }
            case 26: { // saw "\\]" before
                if (c == ']') {
                    observerState.getObserver().foundPattern(4, startPos, pos, observerState);
                    state = 0;
                } else {
                    observerState.getObserver().foundPattern(4, startPos, pos - 1, observerState);
                    state = 0;
                    --pos;
                }
                break;
            }
            case 27: { // saw "&" before
                if (Character.isLetterOrDigit(c) || (c == '#')) {
                    state = 28;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 28: { // saw "&[\\p{AlNum}#]" before
                if (Character.isLetterOrDigit(c)) {
                    state = 29;
                } else {
                    state = 0;
                    --pos;
                }
                // nothing to do for else
                break;
            }
            case 29: { // saw "&[\\p{AlNum}#][\\p{AlNum}]*" before
                if (c == ';') {
                    observerState.getObserver().foundPattern(20, startPos, pos, observerState);
                    state = 0;
                }
                // nothing to do for else
                break;
            }
            case 30: { // saw "[\}]*" before
                if (c != '}') {
                    observerState.getObserver().foundPattern(21, startPos, pos - 1, observerState);
                    state = 0;
                    --pos;
                }
                // nothing to do for else
                break;
            }
            case 31: { // saw "|" before
                if (c == '|') {
                    state = 23;
                } else {
                    state = 0;
                }
                break;
            }
            case 32: { // saw "\\\n[!\\|][^\\\n\\|]*\\|" before
                if (c == '|') {
                    observerState.getObserver().foundPattern(18, startPos, pos, observerState);
                    state = 23;
                    startPos = pos - 1;
                } else {
                    observerState.getObserver().foundPattern(15, startPos, pos - 1, observerState);
                    state = 0;
                    --pos;
                }
                break;
            }
            case 33: { // saw "\\\n[!\\|][^\\\n\\|]*\\{" before
                switch (c) {
                case '}': {
                    state = 23;
                    break;
                }
                case '{': {
                    state = 34;
                    break;
                }
                // nothing to do for default
                }
                break;
            }
            case 34: { // saw "\\\n[!\\|][^\\\n\\|]*\\{\\{" before
                if (c == '}') {
                    state = 33;
                }
                // nothing to do for else
                break;
            }
            case 35: { // saw "\\\n[!\\|][^\\\n\\|]*\\[" before
                switch (c) {
                case ']': {
                    state = 23;
                    break;
                }
                case '[': {
                    state = 36;
                    break;
                }
                // nothing to do for default
                }
                break;
            }
            case 36: { // saw "\\\n[!\\|][^\\\n\\|]*\\[\\[" before
                if (c == ']') {
                    state = 35;
                }
                // nothing to do for else
                break;
            }
            case 37: { // saw Pattern 3 ("\\[\\[[^\\]\\|]\\|") and "[^\\]\\|]*" before
                switch (c) {
                case ']': {
                    state = 38;
                    break;
                }
                case '|': {
                    observerState.getObserver().foundPattern(23, startPos, pos, observerState);
                    startPos = pos + 1;
                    break;
                }
                // nothing to do for default
                }
                break;
            }
            case 38: { // saw Pattern 3 ("\\[\\[[^\\]\\|]\\|") and "[^\\]\\|]*\\]" before
                if (c != ']') {
                    LOGGER.info("There seems to be an error in the wiki markup. Found a link starting with \"[[\" but ending with \"]"
                            + c + "\".");
                    --pos;
                }
                observerState.getObserver().foundPattern(22, startPos, pos, observerState);
                state = 0;
                break;
            }
            case 39: { // saw Pattern 6 ("\\[[^\\] ] ") and "[^\\]]*" before
                if (c == ']') {
                    observerState.getObserver().foundPattern(24, startPos, pos, observerState);
                    state = 0;
                }
                break;
            }
            default:
                LOGGER.error("Unknown state " + state + ".");
                break;
            }
        }

        // check that the machine ends up with the state 0
        if (state != 0) {
            int patternId = -1;
            int pos = chars.length - 1;
            switch (state) {
            // states which are not interesting
            case 9: // falls through
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
            case 21:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 37:
            case 39: {// nothing to do
                break;
            }
            // states which have identified a pattern
            case 2: // falls through
            case 3: {
                patternId = 1;
                break;
            }
            case 4: // falls through
            case 5:
            case 6:
            case 7:
            case 8: {
                patternId = 9;
                break;
            }
            case 10: // falls through
            case 11: {
                patternId = 3;
                ++pos;
                break;
            }
            case 12: {
                patternId = 6;
                ++pos;
                break;
            }
            case 14: {
                patternId = 11;
                break;
            }
            case 16: {
                patternId = 12;
                break;
            }
            case 20: {
                patternId = 13;
                break;
            }
            case 22: {
                patternId = 14;
                break;
            }
            case 23: // falls through
            case 24: {
                patternId = 16;
                break;
            }
            case 25: {
                patternId = 17;
                break;
            }
            case 30: {
                patternId = 21;
                break;
            }
            case 32: {
                patternId = 15;
                break;
            }
            case 33: // falls through
            case 34:
            case 35:
            case 36: {
                patternId = 16;
                break;
            }
            case 38: {
                patternId = 22;
                break;
            }
            default: {
                LOGGER.warn("Machine ended with an unexpected state=" + state + ". This should be 0.");
            }
            }
            // If this last state can be identified as a pattern
            if (patternId > 0) {
                observerState.getObserver().foundPattern(patternId, startPos, pos, observerState);
            }
        }
    }
}
