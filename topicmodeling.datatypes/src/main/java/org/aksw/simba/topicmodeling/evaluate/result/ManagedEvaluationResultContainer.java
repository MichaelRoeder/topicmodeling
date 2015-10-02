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
package org.aksw.simba.topicmodeling.evaluate.result;

//import com.unister.semweb.topicmodeling.testframework.TestFramework;

public class ManagedEvaluationResultContainer {

    private static final boolean COLLECT_SINGLE_EVALUATION_RESULTS = true;
    private static final boolean COLLECT_ONE_DIMENSIONAL_EVALUATION_RESULTS = true;
    // /// DEBUG was set to false because the activities25 corpus is to large to
    // collect this type of result
    private static final boolean COLLECT_TWO_DIMENSIONAL_EVALUATION_RESULTS = false;//!TestFramework.PREVENT_LARGE_DATA_PRODUCTION;

    private static final int NUMBER_OF_DIFFERENT_RESULT_TYPES = EvaluationResultDimension.numberOfDimensions()
            * EvaluationResultDimension.numberOfDimensions() + EvaluationResultDimension.numberOfDimensions() + 1;
    private static final int ONE_DIM_START_INDEX = EvaluationResultDimension.numberOfDimensions()
            * EvaluationResultDimension.numberOfDimensions();
    private static final int SINGLE_RESULT_INDEX = NUMBER_OF_DIFFERENT_RESULT_TYPES - 1;

    protected EvaluationResultCollection results[] = new EvaluationResultCollection[NUMBER_OF_DIFFERENT_RESULT_TYPES];

    public ManagedEvaluationResultContainer() {
        for (int i = 0; i < results.length; i++) {
            results[i] = new EvaluationResultCollection();
        }
    }

    public void addResult(EvaluationResult result) {
        if (result == null) {
            return;
        } else if (result instanceof EvaluationResultCollection) {
            addResults(((EvaluationResultCollection) result));
        } else if (result instanceof SingleEvaluationResult) {
            addResult((SingleEvaluationResult) result);
        } else if (result instanceof OneDimensionalEvaluationResult<?>) {
            addResult((OneDimensionalEvaluationResult<?>) result);
        } else if (result instanceof TwoDimensionalEvaluationResult<?>) {
            addResult((TwoDimensionalEvaluationResult<?>) result);
        }
    }

    public void addResult(SingleEvaluationResult result) {
        if (COLLECT_SINGLE_EVALUATION_RESULTS) {
            results[SINGLE_RESULT_INDEX].addResult(result);
        }
    }

    public void addResult(OneDimensionalEvaluationResult<?> result) {
        if (COLLECT_ONE_DIMENSIONAL_EVALUATION_RESULTS) {
            results[ONE_DIM_START_INDEX + result.getDimension().toInt()].addResult(result);
        }
    }

    public void addResult(TwoDimensionalEvaluationResult<?> result) {
        if (COLLECT_TWO_DIMENSIONAL_EVALUATION_RESULTS) {
            results[(result.getDimension1().toInt() * EvaluationResultDimension.numberOfDimensions())
                    + result.getDimension2().toInt()].addResult(result);
        }
    }

    public void addResults(EvaluationResult results[]) {
        for (int i = 0; i < results.length; i++) {
            addResult(results[i]);
        }
    }

    public void addResults(EvaluationResultCollection results) {
        for (int i = 0; i < results.size(); i++) {
            addResult(results.get(i));
        }
    }

    public EvaluationResultCollection getResultsWithDimension(EvaluationResultDimension dimension) {
        return results[dimension.toInt()];
    }

    public EvaluationResultCollection getResultsWithDimensions(EvaluationResultDimension dimension1,
            EvaluationResultDimension dimension2) {
        return results[(dimension1.toInt() * EvaluationResultDimension.numberOfDimensions()) + dimension2.toInt()];
    }

    public EvaluationResultCollection getSingleResults() {
        return results[SINGLE_RESULT_INDEX];
    }

    public void clear() {
        for (int i = 0; i < results.length; i++) {
            results[i].clear();
        }
    }

    public EvaluationResultCollection[] getAllResults() {
        return results;
    }

    public EvaluationResultCollection[] getOneDimensionalResults() {
        EvaluationResultCollection oneDimResults[] = new EvaluationResultCollection[EvaluationResultDimension
                .numberOfDimensions()];
        System.arraycopy(results, ONE_DIM_START_INDEX, oneDimResults, 0, oneDimResults.length);
        return oneDimResults;
    }

    public EvaluationResultCollection[] getTwoDimensionalResults() {
        EvaluationResultCollection twoDimResults[] = new EvaluationResultCollection[EvaluationResultDimension
                .numberOfDimensions() * EvaluationResultDimension.numberOfDimensions()];
        System.arraycopy(results, 0, twoDimResults, 0, twoDimResults.length);
        return twoDimResults;
    }

    public int getNumberOfOneDimensionalResults() {
        int size = 0;
        for (int i = ONE_DIM_START_INDEX; i < SINGLE_RESULT_INDEX; i++) {
            size += results[i].size();
        }
        return size;
    }

    public int getNumberOfTwoDimensionalResults() {
        int size = 0;
        for (int i = 0; i < ONE_DIM_START_INDEX; i++) {
            size += results[i].size();
        }
        return size;
    }

    public EvaluationResult getEvaluationResultWithName(String name, EvaluationResultDimension... dimension) {
        EvaluationResultCollection collection;
        // find the result collection containing results with the given
        // dimensions
        if (dimension.length > 1) {
            collection = results[dimension[0].toInt() * EvaluationResultDimension.numberOfDimensions()
                    + dimension[1].toInt()];
        } else if (dimension.length == 1) {
            collection = results[ONE_DIM_START_INDEX + dimension[0].toInt()];
        } else {
            collection = results[SINGLE_RESULT_INDEX];
        }
        // search for a result with the given name
        for (int i = 0; i < collection.size(); ++i) {
            if (collection.get(i).getResultName().equals(name)) {
                return collection.get(i);
            }
        }
        return null;
    }
}
