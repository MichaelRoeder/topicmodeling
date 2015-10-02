/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.io.xml.stream;

import java.io.Reader;

import org.aksw.simba.topicmodeling.io.xml.XMLParserObserver;
import org.apache.commons.lang3.StringEscapeUtils;


public class SimpleReaderBasedXMLParser implements ReaderBasedTextMachineObserver {

    private ReaderBasedXMLTextMachine textParser = new ReaderBasedXMLTextMachine();
    private XMLParserObserver observer;
    private Reader reader;
    private StringBuilder dataBuffer = new StringBuilder();

    public SimpleReaderBasedXMLParser(Reader reader, XMLParserObserver observer) {
        this.reader = reader;
        this.observer = observer;
    }

    public void parse() {
        textParser.analyze(reader, this);
    }

    @Override
    public void foundPattern(int patternId, String data, String patternMatch) {
        dataBuffer.append(data);
        if (patternId == ReaderBasedXMLTextMachine.XML_ENCODED_CHAR_PATTERN_ID) {
            dataBuffer.append(StringEscapeUtils.unescapeXml(patternMatch));
        } else if (patternId == ReaderBasedXMLTextMachine.XML_TAG_PATTERN_ID) {
            if (dataBuffer.length() > 0) {
                observer.handleData(dataBuffer.toString());
                dataBuffer.setLength(0);
            }
            if (patternMatch.startsWith("</")) {
                observer.handleClosingTag(patternMatch.substring(2, patternMatch.length() - 1));
            } else {
                if (patternMatch.endsWith("/>")) {

                } else {
                    observer.handleOpeningTag(patternMatch.substring(1, patternMatch.length() - 1));
                }
            }
        }
    }

    public void stop() {
        textParser.stop();
    }
}
