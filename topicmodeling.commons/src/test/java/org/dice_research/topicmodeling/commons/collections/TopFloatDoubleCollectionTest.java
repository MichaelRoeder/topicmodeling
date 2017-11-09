package org.dice_research.topicmodeling.commons.collections;

import java.util.Random;

import org.dice_research.topicmodeling.commons.sort.AssociativeSort;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

public class TopFloatDoubleCollectionTest {

    private static final int DATA_SIZE = 4096;
    private static final int TOP_X = 100;

    @Test
    public void testTopFloatDoubleCollectionDescending() {
        Random rand = new Random(System.currentTimeMillis());
        float values[] = new float[DATA_SIZE];
        double objects[] = new double[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; ++i) {
            values[i] = rand.nextFloat();
            objects[i] = rand.nextDouble();
        }

        TopFloatDoubleCollection topCollection = new TopFloatDoubleCollection(TOP_X, false);
        for (int i = 0; i < values.length; i++) {
            topCollection.add(values[i], objects[i]);
        }

        AssociativeSort.insertionSort(values, objects);

        values = ArrayUtils.subarray(values, DATA_SIZE - TOP_X, DATA_SIZE);
        ArrayUtils.reverse(values);
        Assert.assertArrayEquals(values, topCollection.values, 0);

        objects = ArrayUtils.subarray(objects, DATA_SIZE - TOP_X, DATA_SIZE);

        // Because Insertion sort is stable but sorts ascending the check if the object arrays are equal has to be done
        // manually
        int start = 0;
        int end = 1;
        double objectsSubArray1[], objectsSubArray2[];
        double collectionObjects[] = topCollection.getObjects();
        while (end < collectionObjects.length) {
            while ((end < collectionObjects.length) && (values[start] == values[end])) {
                ++end;
            }
            // If we have reached the end of values, we can not guarantee, that the arrays are equal
            if (end == collectionObjects.length) {
                break;
            }
            objectsSubArray1 = ArrayUtils.subarray(objects, objects.length - end, objects.length - start);
            objectsSubArray2 = ArrayUtils.subarray(collectionObjects, start, end);
            Assert.assertArrayEquals(objectsSubArray1, objectsSubArray2, 0);
            start = end;
            ++end;
        }
    }

    @Test
    public void testTopFloatDoubleCollectionAscending() {
        Random rand = new Random(System.currentTimeMillis());
        float values[] = new float[DATA_SIZE];
        double objects[] = new double[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; ++i) {
            values[i] = rand.nextFloat();
            objects[i] = rand.nextDouble();
        }

        TopFloatDoubleCollection topCollection = new TopFloatDoubleCollection(TOP_X, true);
        for (int i = 0; i < values.length; i++) {
            topCollection.add(values[i], objects[i]);
        }

        AssociativeSort.insertionSort(values, objects);
        values = ArrayUtils.subarray(values, 0, TOP_X);
        objects = ArrayUtils.subarray(objects, 0, TOP_X);

        Assert.assertArrayEquals(values, topCollection.values, 0);
        Assert.assertArrayEquals(objects, topCollection.getObjects(), 0);
    }
}
