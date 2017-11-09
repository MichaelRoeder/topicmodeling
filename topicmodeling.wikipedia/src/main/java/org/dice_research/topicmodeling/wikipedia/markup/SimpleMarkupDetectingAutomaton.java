package org.dice_research.topicmodeling.wikipedia.markup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.BitSet;

public class SimpleMarkupDetectingAutomaton implements MarkupDetectingAutomaton {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleMarkupDetectingAutomaton.class);

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
     * 11 "<[^>]*>"
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
     * there is no pattern # 19
     * 
     * 20 "|}"
     * 
     * 21 "&[^;]*;"
     */
    @Override
    public void analyze(String text,
            MarkupDetectingAutomatonObserverState observerState,
            BitSet patternBitSet) {
        SimpleMarkupDetectingAutomatonState automatonState = new SimpleMarkupDetectingAutomatonState(
                patternBitSet);
        char chars[] = text.toCharArray();
        int pos = 0;
        int startPos = 0;
        int state = 0;
        char c;
        while (pos < chars.length) {
            c = chars[pos];
            switch (state) {
            case 0: {
                switch (c) {
                case '<': {
                    if (automatonState.patternBitSet.get(9)
                            || automatonState.patternBitSet.get(11)) {
                        startPos = pos;
                        state = 1;
                    }
                    break;
                }
                case '&': {
                    if (automatonState.patternBitSet.get(21)) {
                        startPos = pos;
                        state = 5;
                    }
                    break;
                }
                case '{': {
                    if (automatonState.patternBitSet.get(1)
                            || automatonState.patternBitSet.get(15)) {
                        startPos = pos;
                        state = 7;
                    }
                    break;
                }
                case '[': {
                    if (automatonState.patternBitSet.get(3)
                            || automatonState.patternBitSet.get(4)) {
                        startPos = pos;
                        state = 8;
                    }
                    break;
                }
                case '|': {
                    if (automatonState.patternBitSet.get(8)
                            || automatonState.patternBitSet.get(18)
                            || automatonState.patternBitSet.get(20)) {
                        startPos = pos;
                        state = 9;
                    }
                    break;
                }
                case '}': {
                    if (automatonState.patternBitSet.get(2)) {
                        startPos = pos;
                        state = 11;
                    }
                    break;
                }
                case ']': {
                    if (automatonState.patternBitSet.get(5)
                            || automatonState.patternBitSet.get(6)) {
                        startPos = pos;
                        state = 12;
                    }
                    break;
                }
                case '-': {
                    if (automatonState.patternBitSet.get(10)) {
                        startPos = pos;
                        state = 13;
                    }
                    break;
                }
                case '\n': {
                    if (automatonState.patternBitSet.get(7) || automatonState.patternBitSet.get(14)
                            || automatonState.patternBitSet.get(16)
                            || automatonState.patternBitSet.get(17)) {
                        startPos = pos;
                        state = 10;
                    }
                    break;
                }
                case '\'': {
                    if (automatonState.patternBitSet.get(13)) {
                        startPos = pos;
                        state = 17;
                    }
                    break;
                }
                case '=': {
                    if (automatonState.patternBitSet.get(12)) {
                        startPos = pos;
                        state = 19;
                    }
                    break;
                }
                case ' ': {
                    if (automatonState.patternBitSet.get(0)) {
                        observerState.getObserver().foundPattern(0, pos, pos,
                                observerState, automatonState);
                    }
                    break;
                }
                }
                break;
            } // case 0
            case 1: { // saw "<" before
                switch (c) {
                case '!': {
                    if (automatonState.patternBitSet.get(9)) {
                        state = 2;
                    } else {
                        state = 4;
                    }
                    break;
                }
                case '>': {
                    if (automatonState.patternBitSet.get(11)) {
                        observerState.getObserver().foundPattern(11, startPos,
                                pos, observerState, automatonState);
                    }
                    state = 0;
                    break;
                }
                case '<': {
                    startPos = pos;
                    break;
                }
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '/': {
                    if (automatonState.patternBitSet.get(11)) {
                        state = 4;
                    } else {
                        state = 0;
                        --pos;
                    }
                    break;
                }
                default: {
                    state = 0;
                    --pos;
                }
                }
                break;
            }
            case 2: { // saw "<!" before
                switch (c) {
                case '-': {
                    state = 3;
                    break;
                }
                case '>': {
                    if (automatonState.patternBitSet.get(11)) {
                        observerState.getObserver().foundPattern(11, startPos,
                                pos, observerState, automatonState);
                    }
                    state = 0;
                    break;
                }
                case '<': {
                    state = 1;
                    startPos = pos;
                    break;
                }
                default: {
                    if (automatonState.patternBitSet.get(11)) {
                        state = 4;
                    } else {
                        state = 0;
                        --pos;
                    }
                    break;
                }
                }
                break;
            }
            case 3: { // saw "<!-" before
                switch (c) {
                case '-': {
                    observerState.getObserver().foundPattern(9, startPos, pos,
                            observerState, automatonState);
                    state = 0;
                    break;
                }
                case '>': {
                    if (automatonState.patternBitSet.get(11)) {
                        observerState.getObserver().foundPattern(11, startPos,
                                pos, observerState, automatonState);
                    }
                    state = 0;
                    break;
                }
                case '<': {
                    state = 1;
                    startPos = pos;
                    break;
                }
                default: {
                    if (automatonState.patternBitSet.get(11)) {
                        state = 4;
                    } else {
                        state = 0;
                        --pos;
                    }
                    break;
                }
                }
                break;
            }
            case 4: { // saw "<[/a-zA-Z][^>]*" before
                switch (c) {
                case '>': {
                    observerState.getObserver().foundPattern(11, startPos, pos,
                            observerState, automatonState);
                    state = 0;
                    break;
                }
                case '<': {
                    state = 1;
                    startPos = pos;
                    break;
                }
                }
                break;
            }
            case 5: { // saw "&" before
                if (Character.isLetterOrDigit(c) || (c == '#')) {
                    state = 6;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 6: { // saw "&[\\p{AlNum}#][\\p{AlNum}]*" before
                if (c == ';') {
                    observerState.getObserver().foundPattern(21, startPos, pos,
                            observerState, automatonState);
                    state = 0;
                } else if (!Character.isLetterOrDigit(c)) {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 7: { // saw "{" before
                switch (c) {
                case '{': {
                    if (automatonState.patternBitSet.get(1)) {
                        observerState.getObserver().foundPattern(1, startPos,
                                pos, observerState, automatonState);
                    }
                    break;
                }
                case '|': {
                    if (automatonState.patternBitSet.get(15)) {
                        observerState.getObserver().foundPattern(15, startPos,
                                pos, observerState, automatonState);
                    }
                    break;
                }
                default: {
                    --pos;
                }
                }
                state = 0;
                break;
            }
            case 8: { // saw "[" before
                if ((c == '[') && (automatonState.patternBitSet.get(3))) {
                    observerState.getObserver().foundPattern(3, startPos,
                            pos, observerState, automatonState);
                } else {
                    if (automatonState.patternBitSet.get(4)) {
                        observerState.getObserver().foundPattern(4, startPos,
                                pos - 1, observerState, automatonState);
                    }
                    --pos;
                }
                state = 0;
                break;
            }
            case 9: { // saw "|" before
                switch (c) {
                case '|': {
                    if (automatonState.patternBitSet.get(18)) {
                        observerState.getObserver().foundPattern(18, startPos,
                                pos, observerState, automatonState);
                    }
                    break;
                }
                case '}': {
                    if (automatonState.patternBitSet.get(20)) {
                        observerState.getObserver().foundPattern(20, startPos,
                                pos, observerState, automatonState);
                    }
                    break;
                }
                default: {
                    if (automatonState.patternBitSet.get(8)) {
                        observerState.getObserver().foundPattern(8, startPos,
                                pos - 1, observerState, automatonState);
                    }
                    --pos;
                    break;
                }
                }
                state = 0;
                break;
            }
            case 10: { // saw "\n" before
                switch (c) {
                case '*':
                case '#':
                case ':':
                case ';': {
                    if (automatonState.patternBitSet.get(14)) {
                        state = 16;
                    } else {
                        state = 0;
                    }
                    break;
                }
                case '|': {
                    if (automatonState.patternBitSet.get(16) || automatonState.patternBitSet.get(17)) {
                        state = 15;
                    } else {
                        --pos;
                        state = 0;
                    }
                    break;
                }
                case '!': {
                    if (automatonState.patternBitSet.get(16)) {
                        observerState.getObserver().foundPattern(16, startPos, pos, observerState, automatonState);
                    }
                    state = 0;
                    break;
                }
                default: {
                    if (automatonState.patternBitSet.get(7)) {
                        observerState.getObserver().foundPattern(7, startPos, pos - 1, observerState, automatonState);
                    }
                    state = 0;
                    --pos;
                }
                }
                break;
            }
            case 11: { // saw "}" before
                if (c == '}') {
                    observerState.getObserver().foundPattern(2, startPos,
                            pos, observerState, automatonState);
                } else {
                    --pos;
                }
                state = 0;
                break;
            }
            case 12: { // saw "]" before
                if ((c == ']') && (automatonState.patternBitSet.get(5))) {
                    observerState.getObserver().foundPattern(5, startPos,
                            pos, observerState, automatonState);
                } else {
                    if (automatonState.patternBitSet.get(6)) {
                        observerState.getObserver().foundPattern(6, startPos,
                                pos - 1, observerState, automatonState);
                    }
                    --pos;
                }
                state = 0;
                break;
            }
            case 13: { // saw "-" before
                if (c == '-') {
                    state = 14;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 14: { // saw "--" before
                switch (c) {
                case '>': {
                    observerState.getObserver().foundPattern(10, startPos, pos,
                            observerState, automatonState);
                    state = 0;
                    break;
                }
                case '-': {
                    ++startPos;
                    break;
                }
                default: {
                    state = 0;
                    --pos;
                }
                }
                break;
            }
            case 15: { // saw "\n|" before
                boolean patternFound = false;
                switch (c) {
                case '-': {
                    if (automatonState.patternBitSet.get(17)) {
                        observerState.getObserver().foundPattern(17, startPos,
                                pos, observerState, automatonState);
                        patternFound = true;
                    }
                    break;
                }
                case '}': {
                    if (automatonState.patternBitSet.get(20)) {
                        observerState.getObserver().foundPattern(20, startPos,
                                pos, observerState, automatonState);
                        patternFound = true;
                    }
                    break;
                }
                }
                if (!patternFound) {
                    if (automatonState.patternBitSet.get(16)) {
                        observerState.getObserver().foundPattern(16, startPos,
                                pos - 1, observerState, automatonState);
                    }
                    --pos;
                }
                state = 0;
                break;
            }
            case 16: { // saw "\n[*#:;]+" before
                switch (c) {
                case '*':
                case '#':
                case ':':
                case ';': {
                    break;
                }
                default: {
                    observerState.getObserver().foundPattern(14, startPos,
                            pos - 1, observerState, automatonState);
                    state = 0;
                    --pos;
                }
                }
                break;
            }
            case 17: { // saw "'" before
                if (c == '\'') {
                    state = 18;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 18: { // saw "''" before
                if (c == '\'') {
                    observerState.getObserver().foundPattern(13, startPos, pos,
                            observerState, automatonState);
                } else {
                    observerState.getObserver().foundPattern(13, startPos,
                            pos - 1, observerState, automatonState);
                    --pos;
                }
                state = 0;
                break;
            }
            case 19: { // saw "=" before
                if (c == '=') {
                    state = 20;
                } else {
                    state = 0;
                    --pos;
                }
                break;
            }
            case 20: { // saw "==" before
                if (c == '=') {
                    if (pos - startPos > 5) {
                        observerState.getObserver().foundPattern(12, startPos, pos - 1,
                                observerState, automatonState);
                        state = 0;
                    }
                } else {
                    observerState.getObserver().foundPattern(12, startPos,
                            pos - 1, observerState, automatonState);
                    --pos;
                    state = 0;
                }
                break;
            }
            default: {
                LOGGER.error("Unknown state = " + state
                        + "!!! Setting the state back to 0.");
                state = 0;
            }
            } // switch (state)
            ++pos;
        } // while (pos < chars.length)

        // make sure the automaton ends with the correct state
        if (state != 0) {
            switch (state) {
            case 1: // falls through
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 10:
            case 13:
            case 14:
            case 17:
            case 19: { // nothing to do
                break;
            }
            case 8: {
                if (automatonState.patternBitSet.get(4)) {
                    observerState.getObserver().foundPattern(4, startPos, pos,
                            observerState, automatonState);
                }
                break;
            }
            case 9: {
                if (automatonState.patternBitSet.get(8)) {
                    observerState.getObserver().foundPattern(8, startPos, pos,
                            observerState, automatonState);
                }
                break;
            }
            case 11: {
                if (automatonState.patternBitSet.get(20)) {
                    observerState.getObserver().foundPattern(20, startPos, pos,
                            observerState, automatonState);
                }
                break;
            }
            case 12: {
                if (automatonState.patternBitSet.get(6)) {
                    observerState.getObserver().foundPattern(6, startPos, pos,
                            observerState, automatonState);
                }
                break;
            }
            case 15: {
                if (automatonState.patternBitSet.get(16)) {
                    observerState.getObserver().foundPattern(16, startPos, pos,
                            observerState, automatonState);
                }
                break;
            }
            case 16: {
                observerState.getObserver().foundPattern(14, startPos, pos,
                        observerState, automatonState);
                break;
            }
            case 18: {
                observerState.getObserver().foundPattern(13, startPos, pos,
                        observerState, automatonState);
                break;
            }
            case 20: {
                observerState.getObserver().foundPattern(12, startPos, pos,
                        observerState, automatonState);
                break;
            }
            default: {
                LOGGER.error("Ended with unknown state = " + state + "!!!");
            }
            }
        }
    }
}
