package org.aksw.simba.topicmodeling.commons.sort;

/**
 * This class is a container for some static functions to sort associative.
 * 
 * <b>Note</b> that this class has been generated automatically using the {@link AssociativeSortClassGenerator} class.
 * Do never change the source code in this class because changes could be overwritten by the genrating class. Change the
 * source code of the {@link AssociativeSortClassGenerator} class instead.
 * 
 * @author Martin Nettling
 * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>
 */
public class AssociativeSort {

    /**
     * Constant defining at which array size quicksort or insertion sort is used.

     * This parameter was chosen by making performance tests for arrays of the sizes 50 - 500000 and different
     * parameters in the range 2 - 200.
     */
    public static int MIN_SIZE_FOR_QUICKSORT = 36;

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, boolean[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        boolean tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, boolean[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, boolean[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], boolean[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, boolean[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, boolean[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            boolean temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, byte[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        byte tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, byte[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, byte[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], byte[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, byte[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, byte[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            byte temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, char[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        char tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, char[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, char[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], char[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, char[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, char[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            char temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, short[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        short tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, short[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, short[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], short[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, short[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, short[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            short temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, int[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        int tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, int[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, int[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], int[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, int[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, int[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            int temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, long[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        long tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, long[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, long[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], long[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, long[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, long[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            long temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, float[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        float tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, float[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, float[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], float[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, float[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, float[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            float temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(byte[] A, double[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        double tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, double[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] quickSort(byte[] A, double[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(byte A[], double[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, double[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  byte[] insertionSort(byte[] A, double[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            double temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static <T> void swap(byte[] A, T[] Assoc, int i, int j) {
        byte temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        T tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static <T> byte[] quickSort(byte[] A, T[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static <T> byte[] quickSort(byte[] A, T[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static <T> int partition(byte A[], T[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> byte[] insertionSort(byte[] A, T[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> byte[] insertionSort(byte[] A, T[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            byte tmp = A[p];
            T temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, boolean[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        boolean tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, boolean[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, boolean[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], boolean[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, boolean[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, boolean[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            boolean temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, byte[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        byte tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, byte[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, byte[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], byte[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, byte[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, byte[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            byte temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, char[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        char tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, char[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, char[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], char[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, char[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, char[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            char temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, short[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        short tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, short[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, short[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], short[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, short[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, short[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            short temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, int[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        int tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, int[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, int[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], int[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, int[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, int[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            int temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, long[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        long tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, long[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, long[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], long[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, long[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, long[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            long temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, float[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        float tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, float[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, float[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], float[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, float[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, float[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            float temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(char[] A, double[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        double tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, double[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  char[] quickSort(char[] A, double[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(char A[], double[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, double[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  char[] insertionSort(char[] A, double[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            double temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static <T> void swap(char[] A, T[] Assoc, int i, int j) {
        char temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        T tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static <T> char[] quickSort(char[] A, T[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static <T> char[] quickSort(char[] A, T[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static <T> int partition(char A[], T[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        char pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> char[] insertionSort(char[] A, T[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> char[] insertionSort(char[] A, T[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            char tmp = A[p];
            T temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, boolean[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        boolean tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, boolean[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, boolean[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], boolean[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, boolean[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, boolean[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            boolean temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, byte[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        byte tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, byte[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, byte[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], byte[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, byte[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, byte[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            byte temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, char[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        char tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, char[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, char[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], char[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, char[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, char[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            char temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, short[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        short tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, short[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, short[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], short[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, short[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, short[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            short temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, int[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        int tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, int[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, int[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], int[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, int[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, int[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            int temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, long[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        long tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, long[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, long[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], long[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, long[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, long[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            long temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, float[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        float tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, float[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, float[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], float[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, float[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, float[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            float temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(short[] A, double[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        double tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, double[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  short[] quickSort(short[] A, double[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(short A[], double[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, double[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  short[] insertionSort(short[] A, double[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            double temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static <T> void swap(short[] A, T[] Assoc, int i, int j) {
        short temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        T tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static <T> short[] quickSort(short[] A, T[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static <T> short[] quickSort(short[] A, T[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static <T> int partition(short A[], T[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        short pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> short[] insertionSort(short[] A, T[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> short[] insertionSort(short[] A, T[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            short tmp = A[p];
            T temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, boolean[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        boolean tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, boolean[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, boolean[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], boolean[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, boolean[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, boolean[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            boolean temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, byte[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        byte tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, byte[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, byte[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], byte[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, byte[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, byte[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            byte temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, char[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        char tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, char[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, char[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], char[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, char[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, char[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            char temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, short[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        short tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, short[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, short[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], short[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, short[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, short[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            short temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, int[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        int tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, int[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, int[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], int[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, int[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, int[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            int temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, long[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        long tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, long[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, long[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], long[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, long[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, long[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            long temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, float[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        float tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, float[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, float[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], float[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, float[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, float[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            float temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(int[] A, double[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        double tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, double[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  int[] quickSort(int[] A, double[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(int A[], double[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, double[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  int[] insertionSort(int[] A, double[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            double temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static <T> void swap(int[] A, T[] Assoc, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        T tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static <T> int[] quickSort(int[] A, T[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static <T> int[] quickSort(int[] A, T[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static <T> int partition(int A[], T[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        int pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> int[] insertionSort(int[] A, T[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> int[] insertionSort(int[] A, T[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = A[p];
            T temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, boolean[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        boolean tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, boolean[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, boolean[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], boolean[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, boolean[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, boolean[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            boolean temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, byte[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        byte tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, byte[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, byte[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], byte[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, byte[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, byte[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            byte temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, char[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        char tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, char[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, char[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], char[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, char[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, char[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            char temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, short[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        short tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, short[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, short[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], short[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, short[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, short[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            short temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, int[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        int tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, int[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, int[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], int[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, int[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, int[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            int temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, long[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        long tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, long[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, long[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], long[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, long[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, long[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            long temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, float[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        float tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, float[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, float[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], float[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, float[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, float[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            float temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(long[] A, double[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        double tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, double[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  long[] quickSort(long[] A, double[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(long A[], double[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, double[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  long[] insertionSort(long[] A, double[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            double temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static <T> void swap(long[] A, T[] Assoc, int i, int j) {
        long temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        T tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static <T> long[] quickSort(long[] A, T[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static <T> long[] quickSort(long[] A, T[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static <T> int partition(long A[], T[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        long pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> long[] insertionSort(long[] A, T[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> long[] insertionSort(long[] A, T[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            long tmp = A[p];
            T temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, boolean[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        boolean tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, boolean[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, boolean[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], boolean[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, boolean[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, boolean[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            boolean temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, byte[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        byte tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, byte[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, byte[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], byte[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, byte[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, byte[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            byte temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, char[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        char tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, char[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, char[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], char[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, char[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, char[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            char temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, short[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        short tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, short[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, short[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], short[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, short[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, short[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            short temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, int[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        int tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, int[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, int[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], int[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, int[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, int[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            int temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, long[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        long tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, long[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, long[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], long[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, long[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, long[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            long temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, float[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        float tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, float[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, float[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], float[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, float[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, float[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            float temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(float[] A, double[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        double tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, double[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  float[] quickSort(float[] A, double[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(float A[], double[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, double[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  float[] insertionSort(float[] A, double[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            double temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static <T> void swap(float[] A, T[] Assoc, int i, int j) {
        float temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        T tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static <T> float[] quickSort(float[] A, T[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static <T> float[] quickSort(float[] A, T[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static <T> int partition(float A[], T[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        float pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> float[] insertionSort(float[] A, T[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> float[] insertionSort(float[] A, T[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            float tmp = A[p];
            T temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, boolean[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        boolean tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, boolean[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, boolean[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], boolean[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, boolean[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, boolean[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            boolean temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, byte[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        byte tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, byte[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, byte[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], byte[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, byte[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, byte[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            byte temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, char[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        char tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, char[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, char[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], char[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, char[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, char[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            char temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, short[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        short tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, short[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, short[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], short[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, short[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, short[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            short temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, int[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        int tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, int[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, int[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], int[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, int[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, int[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            int temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, long[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        long tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, long[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, long[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], long[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, long[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, long[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            long temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, float[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        float tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, float[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, float[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], float[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, float[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, float[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            float temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static  void swap(double[] A, double[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        double tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, double[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static  double[] quickSort(double[] A, double[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static  int partition(double A[], double[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, double[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static  double[] insertionSort(double[] A, double[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            double temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

    /**
     * Swaps the element at index i with element at index j
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param i
     *            index of first element
     * @param j
     *            index of second element
     */
    public static <T> void swap(double[] A, T[] Assoc, int i, int j) {
        double temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        T tmp = Assoc[i];
        Assoc[i] = Assoc[j];
        Assoc[j] = tmp;
    }

    /**
     * Quicksort for {@link AbstractKVStorable}s. <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @return A reference to the array that was sorted.
     */
    public static <T> double[] quickSort(double[] A, T[] Assoc) {
        quickSort(A, Assoc, 0, A.length - 1);
        return A;
    }

    /**
     * Quicksort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />
     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            The array to be sorted.
     * @param Assoc
     *            The Array to sort associative to the given Array A
     * @param left
     *            The left boundary of what will be sorted.
     * @param right
     *            The right boundary of what will be sorted.
     * @return A reference to the array that was sorted.
     */
    public static <T> double[] quickSort(double[] A, T[] Assoc, int left, int right) {
        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {
            int partitionIndex = partition(A, Assoc, left, right);
            quickSort(A, Assoc, left, partitionIndex - 1);
            quickSort(A, Assoc, partitionIndex, right);
        } else {
            // Do an insertion sort on the subarray
            insertionSort(A, Assoc, left, right);
        }
        return A;
    }

    /**
     * Partitions part of an array of {@link AbstractKVStorable}s. <br />
     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at
     * A[right-1].
     * 
     * @param A
     *            The array to be partitioned.
     * @param Assoc
     *            The associative Array to partitioned
     * @param left
     *            The left bound of the array.
     * @param right
     *            The right bound of the array.
     * @return The index of the pivot after the partition has occured.
     */
    private static <T> int partition(double A[], T[] Assoc, int left, int right) {
        int i = left;
        int j = right;
        double pivot = A[(left + right) / 2];
        while (i <= j) {
            while (A[i] < pivot)
                i++;
            while (A[j] > pivot)
                j--;

            if (i <= j) {
                swap(A, Assoc, i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> double[] insertionSort(double[] A, T[] Assoc) {
        return insertionSort(A, Assoc, 0, A.length - 1);
    }

    /**
     * Insertion sort for AbstractKVStorable. The bounds specify which part of the array is to be sorted.<br />
     * <br />
     * Algorithm of O( n² ). <br />
     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />
     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.
     * 
     * @param A
     *            an array of Comparable items.
     * @param Assoc
     *            The associative Array
     * @param left
     *            the left-most index of the subarray.
     * @param right
     *            the right-most index of the subarray.
     * @return A reference to the array that was sorted.
     */
    public static <T> double[] insertionSort(double[] A, T[] Assoc, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            double tmp = A[p];
            T temp = Assoc[p];
            int j;
            for (j = p; j > left && tmp < A[j - 1]; j--) {
                A[j] = A[j - 1];
                Assoc[j] = Assoc[j - 1];
            }
            A[j] = tmp;
            Assoc[j] = temp;
        }
        return A;
    }

}