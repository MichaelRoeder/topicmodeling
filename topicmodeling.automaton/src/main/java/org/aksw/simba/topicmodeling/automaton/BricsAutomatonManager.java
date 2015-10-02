/**
 * This file is part of topicmodeling.automaton.
 *
 * topicmodeling.automaton is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.automaton is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.automaton.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.automaton;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

public class BricsAutomatonManager extends AbstractMultiPatternAutomaton {

    private RunAutomaton automata[];

    public BricsAutomatonManager(AutomatonCallback callback, String regexes[]) {
        super(callback);
        automata = new RunAutomaton[regexes.length];
        for (int i = 0; i < regexes.length; ++i) {
            automata[i] = new RunAutomaton((new RegExp(regexes[i])).toAutomaton());
        }
    }

    public void parseText(String text) {
        int pos = 0;
        int textLength = text.length();
        if (textLength > 0) {
            int automatonId;
            int length = -1;
            while (pos < textLength) {
                automatonId = 0;
                while ((length < 0) && (automatonId < automata.length)) {
                    length = automata[automatonId].run(text, pos);
                    ++automatonId;
                }
                if (length < 0) {
                    ++pos;
                } else {
                    this.callback.foundPattern(automatonId - 1, pos, length);
                    pos += length;
                    length = -1;
                }
            }
        }
    }
}
