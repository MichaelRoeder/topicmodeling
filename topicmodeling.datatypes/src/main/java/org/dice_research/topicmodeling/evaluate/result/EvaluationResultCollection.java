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
package org.dice_research.topicmodeling.evaluate.result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EvaluationResultCollection implements EvaluationResult, Iterable<EvaluationResult> {

    protected List<EvaluationResult> results = new ArrayList<EvaluationResult>();

    public void addResult(EvaluationResult result) {
        results.add(result);
    }

    public void addResults(EvaluationResult results[]) {
        for (int i = 0; i < results.length; i++) {
            this.results.add(results[i]);
        }
    }

    @Override
    public String toString() {
        String string = "ResultCollection {";
        boolean first = true;
        for (EvaluationResult result : results) {
            if (!first) {
                string += ",";
            } else {
                first = false;
            }
            if (result == null) {
                string += "null,";
            } else {
                string += result.toString() + ",";
            }
        }
        return string + "}";
    }

    public int size() {
        return results.size();
    }

    @Override
    public String getResultName() {
        String desc = "ResultCollection {";
        boolean first = true;
        for (EvaluationResult result : results) {
            if (!first) {
                desc += ",";
            } else {
                first = false;
            }
            if (result == null) {
                desc += "null,";
            } else {
                desc += result.getResultName() + ",";
            }
        }
        return desc + "}";
    }

    @Override
    public Object getResult() {
        return results.toArray();
    }

    public EvaluationResult get(int index) {
        return results.get(index);
    }

    public void clear() {
        results.clear();
    }

    @Override
    public String getResultAsString() {
        String values = "{";
        boolean first = true;
        for (EvaluationResult result : results) {
            if (!first) {
                values += ",";
            } else {
                first = false;
            }
            if (result == null) {
                values += "null,";
            } else {
                values += result.getResultAsString() + ",";
            }
        }
        return values + "}";
    }

    @Override
    public Iterator<EvaluationResult> iterator() {
        return results.iterator();
    }
}
