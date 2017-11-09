package org.dice_research.topicmodeling.commons.sort;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;


/**
 * This class contains JUnit tests for the {@link AssociativeSort} class.
 * 
 * <b>Note</b> that this class has been generated automatically using the {@link AssociativeSortClassGenerator} class.
 * Do never change the source code in this class because changes could be overwritten by the genrating class. Change the
 * source code of the {@link AssociativeSortClassGenerator} class instead.
 * 
 * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>
 */
public class AssociativeSortTest {

    /**
     * Number of swaps which are done to get a random order of the array elements.
     */
    private static final int INITIALE_SWAPS = 5;

    /**
     * Used to get a random order of the array elements.
     */
    private Random rand = new Random();

    @Test
    public void test_byte_byte_Sort() {
        byte[] keyArray = {1, 2, 3, 4};
        byte[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_byte_char_Sort() {
        byte[] keyArray = {1, 2, 3, 4};
        char[] associatedArray = {'a', 'b', 'c', 'd'};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, associatedArray);
    }

    @Test
    public void test_byte_short_Sort() {
        byte[] keyArray = {1, 2, 3, 4};
        short[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_byte_int_Sort() {
        byte[] keyArray = {1, 2, 3, 4};
        int[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_byte_long_Sort() {
        byte[] keyArray = {1, 2, 3, 4};
        long[] associatedArray = {1L, 2L, 3L, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, associatedArray);
    }

    @Test
    public void test_byte_float_Sort() {
        byte[] keyArray = {1, 2, 3, 4};
        float[] associatedArray = {1f, 2f, 3f, 4f};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, associatedArray, 0);
    }

    @Test
    public void test_byte_double_Sort() {
        byte[] keyArray = {1, 2, 3, 4};
        double[] associatedArray = {1d, 2d, 3d, 4d};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, associatedArray, 0);
    }

    @Test
    public void test_char_byte_Sort() {
        char[] keyArray = {'a', 'b', 'c', 'd'};
        byte[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, keyArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_char_char_Sort() {
        char[] keyArray = {'a', 'b', 'c', 'd'};
        char[] associatedArray = {'a', 'b', 'c', 'd'};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, keyArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, associatedArray);
    }

    @Test
    public void test_char_short_Sort() {
        char[] keyArray = {'a', 'b', 'c', 'd'};
        short[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, keyArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_char_int_Sort() {
        char[] keyArray = {'a', 'b', 'c', 'd'};
        int[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, keyArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_char_long_Sort() {
        char[] keyArray = {'a', 'b', 'c', 'd'};
        long[] associatedArray = {1L, 2L, 3L, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, keyArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, associatedArray);
    }

    @Test
    public void test_char_float_Sort() {
        char[] keyArray = {'a', 'b', 'c', 'd'};
        float[] associatedArray = {1f, 2f, 3f, 4f};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, keyArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, associatedArray, 0);
    }

    @Test
    public void test_char_double_Sort() {
        char[] keyArray = {'a', 'b', 'c', 'd'};
        double[] associatedArray = {1d, 2d, 3d, 4d};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, keyArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, associatedArray, 0);
    }

    @Test
    public void test_short_byte_Sort() {
        short[] keyArray = {1, 2, 3, 4};
        byte[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_short_char_Sort() {
        short[] keyArray = {1, 2, 3, 4};
        char[] associatedArray = {'a', 'b', 'c', 'd'};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, associatedArray);
    }

    @Test
    public void test_short_short_Sort() {
        short[] keyArray = {1, 2, 3, 4};
        short[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_short_int_Sort() {
        short[] keyArray = {1, 2, 3, 4};
        int[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_short_long_Sort() {
        short[] keyArray = {1, 2, 3, 4};
        long[] associatedArray = {1L, 2L, 3L, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, associatedArray);
    }

    @Test
    public void test_short_float_Sort() {
        short[] keyArray = {1, 2, 3, 4};
        float[] associatedArray = {1f, 2f, 3f, 4f};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, associatedArray, 0);
    }

    @Test
    public void test_short_double_Sort() {
        short[] keyArray = {1, 2, 3, 4};
        double[] associatedArray = {1d, 2d, 3d, 4d};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, associatedArray, 0);
    }

    @Test
    public void test_int_byte_Sort() {
        int[] keyArray = {1, 2, 3, 4};
        byte[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_int_char_Sort() {
        int[] keyArray = {1, 2, 3, 4};
        char[] associatedArray = {'a', 'b', 'c', 'd'};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, associatedArray);
    }

    @Test
    public void test_int_short_Sort() {
        int[] keyArray = {1, 2, 3, 4};
        short[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_int_int_Sort() {
        int[] keyArray = {1, 2, 3, 4};
        int[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_int_long_Sort() {
        int[] keyArray = {1, 2, 3, 4};
        long[] associatedArray = {1L, 2L, 3L, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, associatedArray);
    }

    @Test
    public void test_int_float_Sort() {
        int[] keyArray = {1, 2, 3, 4};
        float[] associatedArray = {1f, 2f, 3f, 4f};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, associatedArray, 0);
    }

    @Test
    public void test_int_double_Sort() {
        int[] keyArray = {1, 2, 3, 4};
        double[] associatedArray = {1d, 2d, 3d, 4d};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, keyArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, associatedArray, 0);
    }

    @Test
    public void test_long_byte_Sort() {
        long[] keyArray = {1L, 2L, 3L, 4};
        byte[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, keyArray);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_long_char_Sort() {
        long[] keyArray = {1L, 2L, 3L, 4};
        char[] associatedArray = {'a', 'b', 'c', 'd'};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, keyArray);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, associatedArray);
    }

    @Test
    public void test_long_short_Sort() {
        long[] keyArray = {1L, 2L, 3L, 4};
        short[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, keyArray);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_long_int_Sort() {
        long[] keyArray = {1L, 2L, 3L, 4};
        int[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, keyArray);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_long_long_Sort() {
        long[] keyArray = {1L, 2L, 3L, 4};
        long[] associatedArray = {1L, 2L, 3L, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, keyArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, associatedArray);
    }

    @Test
    public void test_long_float_Sort() {
        long[] keyArray = {1L, 2L, 3L, 4};
        float[] associatedArray = {1f, 2f, 3f, 4f};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, keyArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, associatedArray, 0);
    }

    @Test
    public void test_long_double_Sort() {
        long[] keyArray = {1L, 2L, 3L, 4};
        double[] associatedArray = {1d, 2d, 3d, 4d};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, keyArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, associatedArray, 0);
    }

    @Test
    public void test_float_byte_Sort() {
        float[] keyArray = {1f, 2f, 3f, 4f};
        byte[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, keyArray, 0);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_float_char_Sort() {
        float[] keyArray = {1f, 2f, 3f, 4f};
        char[] associatedArray = {'a', 'b', 'c', 'd'};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, keyArray, 0);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, associatedArray);
    }

    @Test
    public void test_float_short_Sort() {
        float[] keyArray = {1f, 2f, 3f, 4f};
        short[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, keyArray, 0);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_float_int_Sort() {
        float[] keyArray = {1f, 2f, 3f, 4f};
        int[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, keyArray, 0);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_float_long_Sort() {
        float[] keyArray = {1f, 2f, 3f, 4f};
        long[] associatedArray = {1L, 2L, 3L, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, keyArray, 0);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, associatedArray);
    }

    @Test
    public void test_float_float_Sort() {
        float[] keyArray = {1f, 2f, 3f, 4f};
        float[] associatedArray = {1f, 2f, 3f, 4f};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, keyArray, 0);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, associatedArray, 0);
    }

    @Test
    public void test_float_double_Sort() {
        float[] keyArray = {1f, 2f, 3f, 4f};
        double[] associatedArray = {1d, 2d, 3d, 4d};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, keyArray, 0);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, associatedArray, 0);
    }

    @Test
    public void test_double_byte_Sort() {
        double[] keyArray = {1d, 2d, 3d, 4d};
        byte[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, keyArray, 0);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_double_char_Sort() {
        double[] keyArray = {1d, 2d, 3d, 4d};
        char[] associatedArray = {'a', 'b', 'c', 'd'};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, keyArray, 0);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, associatedArray);
    }

    @Test
    public void test_double_short_Sort() {
        double[] keyArray = {1d, 2d, 3d, 4d};
        short[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, keyArray, 0);
        Assert.assertArrayEquals(new short[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_double_int_Sort() {
        double[] keyArray = {1d, 2d, 3d, 4d};
        int[] associatedArray = {1, 2, 3, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, keyArray, 0);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, associatedArray);
    }

    @Test
    public void test_double_long_Sort() {
        double[] keyArray = {1d, 2d, 3d, 4d};
        long[] associatedArray = {1L, 2L, 3L, 4};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, keyArray, 0);
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4}, associatedArray);
    }

    @Test
    public void test_double_float_Sort() {
        double[] keyArray = {1d, 2d, 3d, 4d};
        float[] associatedArray = {1f, 2f, 3f, 4f};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, keyArray, 0);
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, associatedArray, 0);
    }

    @Test
    public void test_double_double_Sort() {
        double[] keyArray = {1d, 2d, 3d, 4d};
        double[] associatedArray = {1d, 2d, 3d, 4d};
        // make some swaps
        for (int i = 0; i < INITIALE_SWAPS; ++i) {
            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),
                    rand.nextInt(keyArray.length));
        }
        AssociativeSort.quickSort(keyArray, associatedArray);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, keyArray, 0);
        Assert.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, associatedArray, 0);
    }

}