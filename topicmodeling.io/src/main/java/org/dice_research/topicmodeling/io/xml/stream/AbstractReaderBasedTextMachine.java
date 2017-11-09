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
package org.dice_research.topicmodeling.io.xml.stream;

import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractReaderBasedTextMachine implements ReaderBasedTextMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReaderBasedTextMachine.class);

    private boolean stopMachine;

    @Override
    public void analyze(Reader reader, ReaderBasedTextMachineObserver observer) {
        stopMachine = false;
        try {
            int i = reader.read();
            while (i > 0) {
                processNextChar((char) i, observer);
                if (stopMachine) {
                    return;
                }
                i = reader.read();
            }
        } catch (IOException e) {
            LOGGER.error("Got an Exception while reading from the given stream.", e);
            e.printStackTrace();
        }
    }

    protected abstract void processNextChar(char c, ReaderBasedTextMachineObserver observer);

    @Override
    public void stop() {
        stopMachine = true;
    }

}
