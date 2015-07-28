package org.aksw.simba.topicmodeling.evaluate.result;

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
