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

public enum EvaluationResultDimension {

    DOCUMENT("document"), TOPIC("topic"), WORD("word");

    public static int numberOfDimensions() {
        return values().length;
    }

    protected String name;

    private EvaluationResultDimension(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public int toInt() {
        return this.ordinal();
    }

    public static EvaluationResultDimension getDimensionById(int id) {
        if (id >= values().length) {
            return null;
        } else {
            return values()[id];
        }
    }

    public char getCharForWholeSetOfDimensionValues() {
        return Character.toUpperCase(toString().charAt(0));
    }

    public char getCharForSingleValueOfDimension() {
        return Character.toLowerCase(toString().charAt(0));
    }
}
