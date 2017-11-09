/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.utils.doc.ner;

import java.util.List;

public class NamedEntityCandidate {
    private int startPos;
    private int length;
    private List<String> candidates;

    public NamedEntityCandidate(int startPos, int length, List<String> candidates) {
        this.startPos = startPos;
        this.length = length;
        this.candidates = candidates;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return startPos + length;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }
}
