package org.aksw.simba.topicmodeling.evaluate;

public class StatisticalComputations {

    private static final double LOG_2 = Math.log(2);

    public static final double log2(double value) {
        return Math.log(value) / LOG_2;
    }

    public static double arithmeticMean(double values[]) {
        if (values.length == 0) {
            return 0;
        }
        double sum = 0;
        sum = sumUpUsingBinaryTree(values);
        // for (int i = 0; i < values.length; ++i) {
        // sum += values[i];
        // }
        return sum / values.length;
    }

    public static double expectedValue(double values[], double probabilities[]) {
        assert (values.length == probabilities.length);
        double expValue = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            weightedValues[i] = probabilities[i] * values[i];
            // expValue += probabilities[i] * values[i];
        }
        expValue = sumUpUsingBinaryTree(weightedValues);
        return expValue;
    }

    public static double expectedValue(double values[], double frequencies[],
            double frequencySum) {
        if (frequencySum == 0) {
            return 0;
        }
        assert (values.length == frequencies.length);
        double expValue = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            weightedValues[i] = (frequencies[i] / frequencySum) * values[i];
            // expValue += (frequencies[i] / frequencySum) * values[i];
        }
        expValue = sumUpUsingBinaryTree(weightedValues);
        return expValue;
    }

    public static double expectedValue(double values[], int frequencies[],
            int frequencySum) {
        if (frequencySum == 0) {
            return 0;
        }
        assert (values.length == frequencies.length);
        double castedSum = frequencySum, expValue = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            weightedValues[i] = (frequencies[i] / castedSum) * values[i];
            // expValue += (frequencies[i] / castedSum) * values[i];
        }
        expValue = sumUpUsingBinaryTree(weightedValues);
        return expValue;
    }

    public static double expectedValue(int values[], int frequencies[],
            int frequencySum) {
        if (frequencySum == 0) {
            return 0;
        }
        assert (values.length == frequencies.length);
        double castedSum = frequencySum, expValue = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            weightedValues[i] = (frequencies[i] / castedSum) * values[i];
            // expValue += (frequencies[i] / castedSum) * values[i];
        }
        expValue = sumUpUsingBinaryTree(weightedValues);
        return expValue;
    }

    public static double variance(double values[], double probabilities[],
            double expectedValue) {
        double temp, sum = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            temp = values[i] - expectedValue;
            weightedValues[i] = probabilities[i] * temp * temp;
            // sum += probabilities[i] * temp * temp;
        }
        sum = sumUpUsingBinaryTree(weightedValues);
        return sum;
    }

    public static double variance(double values[], double expectedValue) {
        double temp, sum = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            temp = values[i] - expectedValue;
            weightedValues[i] = temp * temp;
            // sum += probabilities[i] * temp * temp;
        }
        sum = sumUpUsingBinaryTree(weightedValues);
        return sum;
    }

    public static double variance(int values[], double probabilities[],
            double expectedValue) {
        double temp, sum = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            temp = values[i] - expectedValue;
            weightedValues[i] = probabilities[i] * temp * temp;
            // sum += probabilities[i] * temp * temp;
        }
        sum = sumUpUsingBinaryTree(weightedValues);
        return sum;
    }

    public static double variance(int values[], int frequencies[],
            int frequencySum, double expectedValue) {
        if (frequencySum == 0) {
            return 0;
        }
        double temp, castedSum = frequencySum, sum = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            temp = values[i] - expectedValue;
            weightedValues[i] = (frequencies[i] / castedSum) * temp * temp;
            // sum += (frequencies[i] / castedSum) * temp * temp;
        }
        sum = sumUpUsingBinaryTree(weightedValues);
        return sum;
    }

    public static double variance(double values[], int frequencies[],
            int frequencySum, double expectedValue) {
        if (frequencySum == 0) {
            return 0;
        }
        double temp, castedSum = frequencySum, sum = 0;
        double weightedValues[] = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            temp = values[i] - expectedValue;
            weightedValues[i] = (frequencies[i] / castedSum) * temp * temp;
            // sum += (frequencies[i] / castedSum) * temp * temp;
        }
        sum = sumUpUsingBinaryTree(weightedValues);
        return sum;
    }

    public static double standardDeviation(double variance) {
        return Math.sqrt(variance);
    }

    public static double sum(double values[]) {
        // double sum = 0;
        // for (int i = 0; i < values.length; ++i) {
        // sum += values[i];
        // }
        // return sum;
        return sumUpUsingBinaryTree(values);
    }

    public static double sum(int values[]) {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += values[i];
        }
        return sum;
    }

    public static double entropy(double probabilities[]) {
        double entropy = 0;
        double tempArray[] = new double[probabilities.length];
        for (int i = 0; i < probabilities.length; ++i) {
            if (probabilities[i] > 0) {
                tempArray[i] = probabilities[i] * log2(probabilities[i]);
                // entropy += probabilities[i] * log2(probabilities[i]);
            } else {
                tempArray[i] = 0;
            }
        }
        entropy = sumUpUsingBinaryTree(tempArray);
        return entropy == 0 ? entropy : -entropy;
    }

    public static double entropy(double frequencies[], double frequencySum) {
        if (frequencySum == 0) {
            return 0;
        }
        double temp, entropy = 0;
        double tempArray[] = new double[frequencies.length];
        for (int i = 0; i < frequencies.length; ++i) {
            if (frequencies[i] > 0) {
                temp = frequencies[i] / frequencySum;
                tempArray[i] = temp * log2(temp);
                // entropy += temp * log2(temp);
            } else {
                tempArray[i] = 0;
            }
        }
        entropy = sumUpUsingBinaryTree(tempArray);
        return entropy == 0 ? entropy : -entropy;
    }

    public static double entropy(int frequencies[], int frequencySum) {
        if (frequencySum == 0) {
            return 0;
        }
        double temp, castedSum = frequencySum, entropy = 0;
        double tempArray[] = new double[frequencies.length];
        for (int i = 0; i < frequencies.length; ++i) {
            if (frequencies[i] > 0) {
                temp = frequencies[i] / castedSum;
                tempArray[i] = temp * log2(temp);
                // entropy += temp * log2(temp);
            } else {
                tempArray[i] = 0;
            }
        }
        entropy = sumUpUsingBinaryTree(tempArray);
        return entropy == 0 ? entropy : -entropy;
    }

    public static double normalizedEntropy(double probabilities[]) {
        if (probabilities.length == 0) {
            return 0;
        }
        return entropy(probabilities) / log2(probabilities.length);
    }

    public static double normalizedEntropy(double frequencies[],
            double frequencySum) {
        return normalizedEntropy(frequencies, frequencySum, frequencies.length);
    }

    public static double normalizedEntropy(double frequencies[],
            double frequencySum, int numberOfPossibilities) {
        if (numberOfPossibilities == 0) {
            return 0;
        }
        return entropy(frequencies, frequencySum) / log2(numberOfPossibilities);
    }

    public static double normalizedEntropy(int frequencies[], int frequencySum) {
        return normalizedEntropy(frequencies, frequencySum, frequencies.length);
    }

    public static double normalizedEntropy(int frequencies[], int frequencySum,
            int numberOfPossibilities) {
        if (numberOfPossibilities == 0) {
            return 0;
        }
        return entropy(frequencies, frequencySum) / log2(numberOfPossibilities);
    }

    public static double sumUpUsingBinaryTree(double[] values) {
        return sumUpUsingBinaryTree(values, 0, values.length);
    }

    private static double sumUpUsingBinaryTree(double[] values, int startIndex, int endIndex) {
        int length = endIndex - startIndex;
        if (length <= 2) {
            switch (length) {
            case 2:
                return values[startIndex] + values[startIndex + 1];
            case 1:
                return values[startIndex];
            default:
                return 0;
            }
        } else {
            length = length / 2;
            return sumUpUsingBinaryTree(values, startIndex, startIndex + length)
                    + sumUpUsingBinaryTree(values, startIndex + length, endIndex);
        }
    }
}
