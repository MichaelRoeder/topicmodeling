package org.dice_research.topicmodeling.textmachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWikipediaMarkupDetectingMachine extends AbstractSimpleTextMachine {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleWikipediaMarkupDetectingMachine.class);

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
     * 15 "[\\n\\|][\\|\\!][^\\|\\n]*\\|"
     * 
     * 16 "[\\n\\|][\\|\\!][^\\|\\n]*\\n"
     * 
     * 17 "\\n\\|-[^\\n]*\\n"
     * 
     * 18 "\\n\\|[^\\}-]" --> deprecated!
     * 
     * 19 "\\n\\|\\}"
     * 
     * 20 "&[^;]*;"
     * 
     * 21 "\\}\\}"
     */
    private int state = 0;
    private int startPos;

    @Override
    protected void processNextCharacter(char c, int pos) {
        switch (state) {
        case 0: {
            switch (c) {
            case '{': {
                state = 1;
                startPos = pos;
                return;
            }
            case '<': {
                state = 4;
                startPos = pos;
                return;
            }
            case '[': {
                state = 9;
                startPos = pos;
                return;
            }
            case ']': {
                state = 26;
                startPos = pos;
                return;
            }
            case '=': {
                state = 13;
                startPos = pos;
                return;
            }
            case '\'': {
                state = 15;
                startPos = pos;
                return;
            }
            case '-': {
                state = 17;
                startPos = pos;
                return;
            }
            case '\n': {
                state = 19;
                startPos = pos;
                return;
            }
            case '&': {
                state = 27;
                startPos = pos;
                return;
            }
            case '}': {
                state = 30;
                startPos = pos;
                return;
            }
            case '|': {
                state = 31;
                startPos = pos;
                return;
            }
            default: {
                // nothing to do for default
                return;
            }
            }
        }
        case 1: { // saw "\\{" before
            if (c == '{') {
                state = 2;
            } else {
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 2: { // saw "\\{\\{" before
            if (c == '}') {
                state = 3;
            }
            // nothing to do for else
            return;
        }
        case 3: { // saw "\\{\\{[^\\}]*}}*" before
            if (c != '}') {
                foundPattern(1, startPos, pos - 1);
                state = 0;
                processNextCharacter(c, pos);
            }
            // nothing to do for else
            return;
        }
        case 4: { // saw "<" before
            switch (c) {
            case '!': {
                state = 5;
                return;
            }
            case '/': {
                state = 7;
                return;
            }
            default: {
                state = 8;
                return;
            }
            }
        }
        case 5: { // saw "<!" before
            switch (c) {
            case '-': {
                state = 5;
                return;
            }
            case '>': {
                foundPattern(9, startPos, pos);
                state = 0;
                return;
            }
            default: {
                state = 8;
                return;
            }
            }
        }
        case 6: { // saw "<!-" before
            switch (c) {
            case '-': {
                foundPattern(7, startPos, pos);
                state = 0;
                return;
            }
            case '>': {
                foundPattern(9, startPos, pos);
                state = 0;
                return;
            }
            default: {
                state = 8;
                return;
            }
            }
        }
        case 7: { // saw "</[^>]*" before
            if (c == '>') {
                foundPattern(9, startPos, pos);
                state = 0;
            }
            // nothing to do for else
            return;
        }
        case 8: { // saw "<[^>]*" before
            if (c == '>') {
                foundPattern(9, startPos, pos);
                state = 0;
            }
            return;
        }
        case 9: { // saw "[" before
            if (c == '[') {
                state = 10;
            } else {
                state = 12;
            }
            return;
        }
        case 10: { // saw "\\[\\[[^\\]]*" before
            switch (c) {
            case ']': {
                state = 11;
                return;
            }
            case '|': {
                foundPattern(3, startPos, pos);
                state = 0;
                return;
            }
            default: {
                // nothing to do for default
                return;
            }
            }
        }
        case 11: { // saw "\\[\\[[^\\]]*\\]" before
            if (c == ']') {
                foundPattern(2, startPos, pos);
                state = 0;
            } else {
                foundPattern(5, startPos, pos - 1);
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 12: { // saw "\\[[^\\[\\]\\|][^\\]\\|]*" before
            switch (c) {
            case ']': {
                foundPattern(5, startPos, pos);
                state = 0;
                return;
            }
            case ' ': {
                foundPattern(6, startPos, pos);
                state = 0;
                return;
            }
            default: {
                // nothing to do for default
                return;
            }
            }
        }
        case 13: { // saw "=" before
            if (c == '=') {
                state = 14;
            } else {
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 14: { // saw "==*" before
            if (c == '=') {
                if ((pos - startPos) > 6) {
                    foundPattern(11, startPos, pos - 1);
                    state = 0;
                    processNextCharacter(c, pos);
                }
                return;
            } else {
                foundPattern(11, startPos, pos - 1);
                state = 0;
                processNextCharacter(c, pos);
                return;
            }
        }
        case 15: { // saw "'" before
            if (c == '\'') {
                state = 16;
            } else {
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 16: { // saw "''*" before
            if (c == '\'') {
                if ((pos - startPos) > 5) {
                    foundPattern(12, startPos, pos - 1);
                    state = 0;
                    processNextCharacter(c, pos);
                }
                return;
            } else {
                foundPattern(12, startPos, pos - 1);
                state = 0;
                processNextCharacter(c, pos);
                return;
            }
        }
        case 17: { // saw "-" before
            if (c == '-') {
                state = 18;
            } else {
                state = 0;
                processNextCharacter(c, pos);
            }
        }
        case 18: { // saw "--" before
            if (c == '>') {
                foundPattern(8, startPos, pos - 1);
            } else {
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 19: { // saw "\n" before
            switch (c) {
            case '*': // falls through
            case '#': // falls through
            case ';': // falls through
            case ':': {
                state = 20;
                return;
            }
            case '{': {
                state = 21;
                return;
            }
            case '!': {
                state = 23;
                return;
            }
            case '|': {
                state = 24;
                return;
            }
            default: {
                state = 0;
                processNextCharacter(c, pos);
                return;
            }
            }
        }
        case 20: {
            if ((c != '*') && (c != '#') && (c != ';') && (c != ':')) {
                foundPattern(13, startPos, pos - 1);
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 21: { // saw "\\\n\\{" before
            switch (c) {
            case '{': {
                state = 2;
                ++startPos;
                return;
            }
            case '|': {
                state = 22;
                return;
            }
            default: {
                state = 0;
                processNextCharacter(c, pos);
                return;
            }
            }
        }
        case 22: { // saw "\\\n\\{\\|[^\\\n]*" before
            if (c == '\n') {
                foundPattern(14, startPos, pos);
                state = 19;
                startPos = pos;
            }
            // nothing to do for else
            return;
        }
        case 23: { // saw "\\\n[!\\|][^\\\n\\|]" before
            switch (c) {
            case '|': {
                foundPattern(15, startPos, pos);
                state = 31; // could be "||"
                return;
            }
            case '\n': {
                foundPattern(16, startPos, pos);
                state = 19;
                startPos = pos;
                return;
            }
            default: {
                // nothing to do for default
                return;
            }
            }
        }
        case 24: { // saw "\\\n\\|" before
            switch (c) {
            case '-': {
                state = 25;
                return;
            }
            case '}': {
                foundPattern(19, startPos, pos);
                state = 0;
                return;
            }
            case '\n': {
                foundPattern(16, startPos, pos);
                state = 19;
                startPos = pos;
                return;
            }
            default: {
//                foundPattern(18, startPos, pos - 1);
//                state = 0;
//                processNextCharacter(c, pos);
                state=23;
                return;
            }
            }
        }
        case 25: { // saw "\\\n\\{\\|-[^\\\n]*" before
            if (c == '\n') {
                foundPattern(17, startPos, pos);
                state = 19;
                startPos = pos;
            }
            // nothing to do for else
            return;
        }
        case 26: { // saw "\\]" before
            if (c == ']') {
                foundPattern(4, startPos, pos);
                state = 0;
            } else {
                foundPattern(4, startPos, pos - 1);
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 27: { // saw "&" before
            if (Character.isLetterOrDigit(c) || (c == '#')) {
                state = 28;
            } else {
                state = 0;
                processNextCharacter(c, pos);
            }
            return;
        }
        case 28: { // saw "&[\\p{AlNum}#]" before
            if (Character.isLetterOrDigit(c)) {
                state = 29;
            } else {
                state = 0;
                processNextCharacter(c, pos);
            }
            // nothing to do for else
            return;
        }
        case 29: { // saw "&[\\p{AlNum}#][\\p{AlNum}]*" before
            if (c == ';') {
                foundPattern(20, startPos, pos);
                state = 0;
            }
            // nothing to do for else
            return;
        }
        case 30: { // saw "[\}]*" before
            if (c != '}') {
                foundPattern(21, startPos, pos - 1);
                state = 0;
                processNextCharacter(c, pos);
            }
            // nothing to do for else
            return;
        }
        case 31: { // saw "|" before
            if (c != '|') {
                state = 23;
            } else {
                state = 0;
            }
            // nothing to do for else
            return;
        }
        default:
            LOGGER.error("Unknown state " + state + ".");
            return;
        }
    }

    @Override
    protected void reset() {
        state = 0;
        startPos = 0;
    }
}
