package org.aksw.simba.topicmodeling.io.reuters;

import org.apache.commons.lang3.StringEscapeUtils;

public class ReutersStringParser {

    public String parseString(String s) {
        StringBuilder newString = new StringBuilder();
        char chars[] = s.toCharArray();
        char c;
        /*
         * 0 - normal state
         * 1 - saw "&" before
         * 2 - saw "&[#A-Za-z]" before
         * 3 - saw a whitespace character before
         */
        int state = 0;
        int diffToPos = 0;
        for (int pos = 0; pos < chars.length; ++pos) {
            c = chars[pos];
            switch (state) {
            case 0: {
                switch (c) {
                case '\r':
                case '\n':
                case '\t':
                case 0xA0:
                case ' ': {
                    newString.append(' ');
                    state = 3;
                    break;
                }
                case '&': {
                    state = 1;
                    break;
                }
                default: {
                    newString.append(c);
                    break;
                }
                }
                break;
            }
            case 1: {
                switch (c) {
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
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '#': {
                    state = 2;
                    diffToPos = 2;
                    break;
                }
                case '\r':
                case '\n':
                case '\t':
                case 0xA0:
                case ' ': {
                    newString.append(chars[pos - 1]);
                    newString.append(' ');
                    state = 3;
                    break;
                }
                default: {
                    newString.append(chars[pos - 1]);
                    newString.append(c);
                    state = 0;
                }
                }
                break;
            }
            case 2: {
                if (diffToPos > 7) {
                    // no encoded character has such a long encoding
                    newString.append(s.substring(pos - diffToPos, pos + 1));
                    state = 0;
                    break;
                }
                switch (c) {
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
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    ++diffToPos;
                    break;
                }
                case ';': {
                    newString.append(StringEscapeUtils.unescapeHtml4(s.substring(pos - diffToPos, pos + 1)));
                    state = 0;
                    break;
                }
                case '\r':
                case '\n':
                case '\t':
                case 0xA0:
                case ' ': {
                    newString.append(s.substring(pos - diffToPos, pos + 1));
                    state = 3;
                    break;
                }
                default: {
                    newString.append(s.substring(pos - diffToPos, pos + 1));
                    state = 0;
                }
                }
                break;
            }
            case 3: {
                switch (c) {
                case '\r':
                case '\n':
                case '\t':
                case 0xA0:
                case ' ': {
                    // nothing to do
                    break;
                }
                case '&': {
                    state = 1;
                    break;
                }
                default: {
                    newString.append(c);
                    state = 0;
                    break;
                }
                }
                break;
            }
            } // switch (state)
        }

        return newString.toString();
    }
}
