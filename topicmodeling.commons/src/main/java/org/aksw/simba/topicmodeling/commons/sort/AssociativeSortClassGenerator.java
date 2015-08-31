package org.aksw.simba.topicmodeling.commons.sort;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class is used to generate the classes {@link AssociativeSort} and
 * {@link AssociativeSortTest} containing the JUnit tests for the
 * {@link AssociativeSort} class. It should only be run if these classes should
 * be changed.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
class AssociativeSortClassGenerator {

    /**
     * The path to the .java file of the {@link AssociativeSort} class.
     */
    private static final String PATH_TO_ASSOCIATIVE_SORT_CLASS_FILE = "src/main/java/org/aksw/simba/topicmodeling/commons/sort/AssociativeSort.java";

    /**
     * The path to the .java file of the JUnit test class
     * {@link AssociativeSortTest}.
     */
    private static final String PATH_TO_ASSOCIATIVE_SORT_TEST_CLASS_FILE = "src/test/java/org/aksw/simba/topicmodeling/commons/sort/AssociativeSortTest.java";

    /**
     * Defining the String which will be replaced with the type of the sorted
     * array.
     */
    private static final String SORTED_TYPE_KEY = "$SORTED_TYPE$";

    /**
     * Defining the String which will be replaced with the type of the
     * associated array.
     */
    private static final String ASSOCIATED_TYPE_KEY = "$ASSOCIATED_TYPE$";

    /**
     * Defining the String which will be replaced with the generic type
     * declaration (or with an empty String if the generic type declaration is
     * not needed).
     */
    private static final String GENERIC_DECLARATION_KEY = "$GENERIC_TYPE$";

    /**
     * Defining the String which will be replaced with the name of the test
     * method.
     */
    private static final String TEST_METHOD_NAME_KEY = "$TEST_NAME$";

    /**
     * Defining the String which will be replaced with the delta which is needed
     * to compare double or float values of the sorted array in the JUnit tests
     * (or an empty String if the type of the sorted array is not double or
     * float).
     */
    private static final String ARRAY_EQUALS_DELTA_SORTED_ARRAY_KEY = "$SORTED_DELTA$";

    /**
     * Defining the String which will be replaced with the delta which is needed
     * to compare double or float values of the associated array in the JUnit
     * tests (or an empty String if the type of the associated array is not
     * double or float).
     */
    private static final String ARRAY_EQUALS_DELTA_ASSOCIATED_ARRAY_KEY = "$ASSOCIATED_DELTA$";

    /**
     * Defining the String which will be replaced with the example array for the
     * sorted array.
     */
    private static final String SORTED_TYPE_EXAMPLE_KEY = "$SORTED_EXAMPLE$";

    /**
     * Defining the String which will be replaced with the example array for the
     * associated array.
     */
    private static final String ASSOCIATED_TYPE_EXAMPLE_KEY = "$ASSOCIATED_EXAMPLE$";

    /**
     * Defining the types the sorted array can have.
     */
    private static final String primitiveSortTypes[] = { "byte", "char", "short", "int", "long", "float", "double" };

    /**
     * Defining the types the associated array can have.
     */
    private static final String associatedTypes[] = { "boolean", "byte", "char", "short", "int", "long", "float",
            "double" };

    /**
     * Defining the sorted example arrays used for the JUnit tests.
     */
    @SuppressWarnings("serial")
    private static final HashMap<String, String> exampleArrays = new HashMap<String, String>() {
        {
            // put("boolean", "{true, true, false, false}");
            put("byte", "{1, 2, 3, 4}");
            put("char", "{'a', 'b', 'c', 'd'}");
            put("double", "{1d, 2d, 3d, 4d}");
            put("float", "{1f, 2f, 3f, 4f}");
            put("int", "{1, 2, 3, 4}");
            put("long", "{1L, 2L, 3L, 4}");
            put("short", "{1, 2, 3, 4}");
        }
    };

    /**
     * FileWriter used for creating the files.
     */
    private FileWriter writer;

    /**
     * Starts the generation.
     * 
     * @param args
     */
    public static void main(String[] args) {

        AssociativeSortClassGenerator generator = new AssociativeSortClassGenerator();
        try {
            generator.writeAssociativeSortClasses(primitiveSortTypes, associatedTypes, exampleArrays);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method writes the two classes.
     * 
     * @param primitiveSortTypes
     *            array containing the types for the sorted array
     * @param associatedTypes
     *            array containing the types for the associated array
     * @param exampleArrays
     *            Map containing the types and their example arrays
     * @throws IOException
     *             thrown by the used {@link #writer}
     */
    private void writeAssociativeSortClasses(String primitiveSortTypes[], String associatedTypes[],
            HashMap<String, String> exampleArrays) throws IOException {
        File file = new File(PATH_TO_ASSOCIATIVE_SORT_CLASS_FILE);
        prepareFile(file);
        writer = new FileWriter(file);
        writer.append(CLASS_HEAD);
        writeMethods(primitiveSortTypes, associatedTypes);
        writer.append(CLASS_FEAT);
        writer.close();

        file = new File(PATH_TO_ASSOCIATIVE_SORT_TEST_CLASS_FILE);
        prepareFile(file);
        writer = new FileWriter(file);
        writer.append(TEST_CLASS_HEAD);
        writeTestMethods(primitiveSortTypes, associatedTypes, exampleArrays);
        writer.append(TEST_CLASS_FEAT);
        writer.close();
    }

    private void prepareFile(File file) {
        File parent = file.getAbsoluteFile();
        parent = parent.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }

    /**
     * This method writes the methods of the {@link AssociativeSort} class for
     * the combinations of the given types. Additionally for every sorted type
     * methods for a generic associated type are generated.
     * 
     * @param primitiveSortTypes
     *            types the sorted arrays can have
     * @param associatedTypes
     *            types the associated arrays can have
     * @throws IOException
     */
    private void writeMethods(String primitiveSortTypes[], String associatedTypes[]) throws IOException {
        for (int i = 0; i < primitiveSortTypes.length; i++) {
            for (int j = 0; j < associatedTypes.length; j++) {
                writeMethods(primitiveSortTypes[i], associatedTypes[j]);
            }
            writeMethods(primitiveSortTypes[i], "T", "<T>");
        }
    }

    /**
     * Writes the methods for a given type pair.
     * 
     * @param sortedType
     *            type the sorted array can have
     * @param associatedType
     *            types the associated array can have
     * @throws IOException
     */
    private void writeMethods(String sortedType, String associatedType) throws IOException {
        writeMethods(sortedType, associatedType, "");
    }

    /**
     * Writes the methods for a given type pair using the given generic
     * declaration.
     * 
     * @param sortedType
     *            type the sorted array can have
     * @param associatedType
     *            types the associated array can have
     * @param genericDeclaration
     *            String declaring a generic or an empty String
     * @throws IOException
     */
    private void writeMethods(String sortedType, String associatedType, String genericDeclaration) throws IOException {
        // void swap(type[] A, type[] Assoc, int i, int j)
        writeMethod(SWAP_METHOD, sortedType, associatedType, genericDeclaration);
        // int[] quickSort(type[] A, type[] Assoc)
        writeMethod(QUICK_SORT_METHOD1, sortedType, associatedType, genericDeclaration);
        // int[] quickSort(int[] A, Object[] Assoc, int left, int right)
        writeMethod(QUICK_SORT_METHOD2, sortedType, associatedType, genericDeclaration);
        // int partition(int A[], Object[] Assoc, int left, int right)
        writeMethod(PARTITION_METHOD, sortedType, associatedType, genericDeclaration);
        // void insertionSort(int[] A, Object[] Assoc)
        writeMethod(INSERTION_SORT_METHOD1, sortedType, associatedType, genericDeclaration);
        // void insertionSort(int[] A, Object[] Assoc, int left, int right)
        writeMethod(INSERTION_SORT_METHOD2, sortedType, associatedType, genericDeclaration);
    }

    /**
     * Method which writes the given method replacing the
     * {@link #SORTED_TYPE_KEY}, the {@link #ASSOCIATED_TYPE_KEY}, and the
     * {@link #GENERIC_DECLARATION_KEY} using the given values.
     * 
     * @param method
     *            method which should be written
     * @param sortedType
     *            type of the sorted array which should be inserted into the
     *            method
     * @param associatedType
     *            type of the associated array which should be inserted into the
     *            method
     * @param genericDeclaration
     *            generic type declaration which should be inserted into the
     *            method
     * @throws IOException
     */
    private void writeMethod(String method, String sortedType, String associatedType, String genericDeclaration)
            throws IOException {
        writer.append(method.replace(SORTED_TYPE_KEY, sortedType).replace(ASSOCIATED_TYPE_KEY, associatedType)
                .replace(GENERIC_DECLARATION_KEY, genericDeclaration));
    }

    /**
     * This method writes the test methods for the combinations of the given
     * example arrays and the types of the sorted and the associated array. Note
     * that a test method is written only if there are examples for the sorted
     * and for the associated array type.
     * 
     * @param primitiveSortTypes
     *            types of the sorted arrays
     * @param associatedTypes
     *            types of the associated arrays
     * @param exampleArrays
     *            examples for the different array types
     * @throws IOException
     */
    private void writeTestMethods(String primitiveSortTypes[], String associatedTypes[],
            HashMap<String, String> exampleArrays) throws IOException {
        String sortedTypeExample, associatedTypeExample;
        for (int i = 0; i < primitiveSortTypes.length; i++) {
            sortedTypeExample = exampleArrays.get(primitiveSortTypes[i]);
            if (sortedTypeExample != null) {
                for (int j = 0; j < associatedTypes.length; j++) {
                    associatedTypeExample = exampleArrays.get(associatedTypes[j]);
                    if (associatedTypeExample != null) {
                        writeTestMethod(primitiveSortTypes[i], sortedTypeExample, associatedTypes[j],
                                associatedTypeExample);
                    } else {
                        System.out.println("There is no example for the type " + associatedTypes[j]
                                + "! Can't generate Testcase.");
                    }
                }
            } else {
                System.out.println(
                        "There is no example for the type " + primitiveSortTypes[i] + "! Can't generate Testcase.");
            }
        }
    }

    /**
     * writes the test method for a single combination of a sorted array type
     * and an associated array type.
     * 
     * @param sortedType
     *            type of the sorted array
     * @param sortedTypeExample
     *            already sorted example of the sorted array type
     * @param associatedType
     *            type of the associated array
     * @param associatedTypeExample
     *            example of the associated array type
     * @throws IOException
     */
    private void writeTestMethod(String sortedType, String sortedTypeExample, String associatedType,
            String associatedTypeExample) throws IOException {
        String methodName = "_" + sortedType + "_" + associatedType + "_";
        String deltaSortedArray = getArrayDelta(sortedType);
        String deltaAssociatedArray = getArrayDelta(associatedType);
        writer.append(TEST_METHOD.replace(SORTED_TYPE_KEY, sortedType)
                .replace(SORTED_TYPE_EXAMPLE_KEY, sortedTypeExample).replace(ASSOCIATED_TYPE_KEY, associatedType)
                .replace(ASSOCIATED_TYPE_EXAMPLE_KEY, associatedTypeExample).replace(TEST_METHOD_NAME_KEY, methodName)
                .replace(ARRAY_EQUALS_DELTA_SORTED_ARRAY_KEY, deltaSortedArray)
                .replace(ARRAY_EQUALS_DELTA_ASSOCIATED_ARRAY_KEY, deltaAssociatedArray));
    }

    /**
     * Returns a String defining a delta for the equal assertion of the JUnit
     * tests if the given type is double or float. Else an empty String is
     * returned.
     * 
     * @param type
     * @return
     */
    private String getArrayDelta(String type) {
        if (type.equals("double") || type.equals("float")) {
            return ", 0";
        } else {
            return "";
        }
    }

    /**
     * Head of the {@link AssociativeSort} class.
     * 
     */
    private static final String CLASS_HEAD = "package org.aksw.simba.topicmodeling.commons.sort;\n" + "\n" + "/**\n"
            + " * This class is a container for some static functions to sort associative.\n" + " * \n"
            + " * <b>Note</b> that this class has been generated automatically using the {@link AssociativeSortClassGenerator} class.\n"
            + " * Do never change the source code in this class because changes could be overwritten by the genrating class. Change the\n"
            + " * source code of the {@link AssociativeSortClassGenerator} class instead.\n" + " * \n"
            + " * @author Martin Nettling\n" + " * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>\n"
            + " */\n" + "public class AssociativeSort {\n\n" + "    /**\n"
            + "     * Constant defining at which array size quicksort or insertion sort is used.\n\n"
            + "     * This parameter was chosen by making performance tests for arrays of the sizes 50 - 500000 and different\n"
            + "     * parameters in the range 2 - 200.\n" + "     */\n"
            + "    public static int MIN_SIZE_FOR_QUICKSORT = 36;\n\n";

    /**
     * String defining the swap method
     */
    private static final String SWAP_METHOD = "    /**\n"
            + "     * Swaps the element at index i with element at index j\n" + "     * \n" + "     * @param A\n"
            + "     *            The array to be sorted.\n" + "     * @param Assoc\n"
            + "     *            The Array to sort associative to the given Array A\n" + "     * @param i\n"
            + "     *            index of first element\n" + "     * @param j\n"
            + "     *            index of second element\n" + "     */\n" + "    public static "
            + GENERIC_DECLARATION_KEY + " void swap(" + SORTED_TYPE_KEY + "[] A, " + ASSOCIATED_TYPE_KEY
            + "[] Assoc, int i, int j) {\n" + "        " + SORTED_TYPE_KEY + " temp = A[i];\n"
            + "        A[i] = A[j];\n" + "        A[j] = temp;\n" + "\n" + "        " + ASSOCIATED_TYPE_KEY
            + " tmp = Assoc[i];\n" + "        Assoc[i] = Assoc[j];\n" + "        Assoc[j] = tmp;\n" + "    }\n\n";

    /**
     * String defining the quick sort method
     */
    private static final String QUICK_SORT_METHOD1 = "    /**\n"
            + "     * Quicksort for two arrays. The first array will be sorted. All swaps that\n"
            + "     * are done to this array during the sorting will be done to the second,\n"
            + "     * associative array, too. <br />\n" + "     * Algorithm of O( n*log(n) ) asymptotic upper bound.\n"
            + "     * \n" + "     * @param A\n" + "     *            The array to be sorted.\n"
            + "     * @param Assoc\n" + "     *            The Array to sort associative to the given Array A\n"
            + "     * @return A reference to the array that was sorted.\n" + "     */\n" + "    public static "
            + GENERIC_DECLARATION_KEY + " " + SORTED_TYPE_KEY + "[] quickSort(" + SORTED_TYPE_KEY + "[] A, "
            + ASSOCIATED_TYPE_KEY + "[] Assoc) {\n" + "        quickSort(A, Assoc, 0, A.length - 1);\n"
            + "        return A;\n" + "    }\n\n";

    /**
     * String defining the quick sort method
     */
    private static final String QUICK_SORT_METHOD2 = "    /**\n"
            + "     * Quicksort. The bounds specify which part of the array is to be sorted.<br />\n"
            + "     * <br />\n" + "     * Algorithm of O( n*log(n) ) asymptotic upper bound. <br />\n"
            + "     * This version of quicksort also allows for bounds to be put in to specify what part of the array will be sorted. <br />\n"
            + "     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.\n"
            + "     * \n" + "     * @param A\n" + "     *            The array to be sorted.\n"
            + "     * @param Assoc\n" + "     *            The Array to sort associative to the given Array A\n"
            + "     * @param left\n" + "     *            The left boundary of what will be sorted.\n"
            + "     * @param right\n" + "     *            The right boundary of what will be sorted.\n"
            + "     * @return A reference to the array that was sorted.\n" + "     */\n" + "    public static "
            + GENERIC_DECLARATION_KEY + " " + SORTED_TYPE_KEY + "[] quickSort(" + SORTED_TYPE_KEY + "[] A, "
            + ASSOCIATED_TYPE_KEY + "[] Assoc, int left, int right) {\n"
            + "        if (left + MIN_SIZE_FOR_QUICKSORT <= right) {\n"
            + "            int partitionIndex = partition(A, Assoc, left, right);\n"
            + "            quickSort(A, Assoc, left, partitionIndex - 1);\n"
            + "            quickSort(A, Assoc, partitionIndex, right);\n" + "        } else {\n"
            + "            // Do an insertion sort on the subarray\n"
            + "            insertionSort(A, Assoc, left, right);\n" + "        }\n" + "        return A;\n"
            + "    }\n\n";

    /**
     * String defining the partitioning method
     */
    private static final String PARTITION_METHOD = "    /**\n"
            + "     * Partitions part of an array. <br />\n"
            + "     * The part of the array between <b>left</b> and <b>right</b> will be partitioned around the value held at\n"
            + "     * A[right-1].\n" + "     * \n" + "     * @param A\n"
            + "     *            The array to be partitioned.\n" + "     * @param Assoc\n"
            + "     *            The associative Array to partitioned\n" + "     * @param left\n"
            + "     *            The left bound of the array.\n" + "     * @param right\n"
            + "     *            The right bound of the array.\n"
            + "     * @return The index of the pivot after the partition has occured.\n" + "     */\n"
            + "    private static " + GENERIC_DECLARATION_KEY + " int partition(" + SORTED_TYPE_KEY + " A[], "
            + ASSOCIATED_TYPE_KEY + "[] Assoc, int left, int right) {\n" + "        int i = left;\n"
            + "        int j = right;\n" + "        " + SORTED_TYPE_KEY + " pivot = A[(left + right) / 2];\n"
            + "        while (i <= j) {\n" + "            while (A[i] < pivot)\n" + "                i++;\n"
            + "            while (A[j] > pivot)\n" + "                j--;\n" + "\n" + "            if (i <= j) {\n"
            + "                swap(A, Assoc, i, j);\n" + "                i++;\n" + "                j--;\n"
            + "            }\n" + "        }\n" + "\n" + "        return i;\n" + "    }\n\n";

    /**
     * String defining the insertion sort.
     */
    private static final String INSERTION_SORT_METHOD1 = "    /**\n"
            + "     * Insertion sort. The bounds specify which part of the array is to be sorted.<br />\n"
            + "     * <br />\n" + "     * Algorithm of O( n² ). <br />\n"
            + "     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />\n"
            + "     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.\n"
            + "     * \n" + "     * @param A\n" + "     *            an array of Comparable items.\n"
            + "     * @param Assoc\n" + "     *            The associative Array\n" + "     * @param left\n"
            + "     *            the left-most index of the subarray.\n" + "     * @param right\n"
            + "     *            the right-most index of the subarray.\n"
            + "     * @return A reference to the array that was sorted.\n" + "     */\n" + "    public static "
            + GENERIC_DECLARATION_KEY + " " + SORTED_TYPE_KEY + "[] insertionSort(" + SORTED_TYPE_KEY + "[] A, "
            + ASSOCIATED_TYPE_KEY + "[] Assoc) {\n" + "        return insertionSort(A, Assoc, 0, A.length - 1);\n"
            + "    }\n\n";

    /**
     * String defining the insertion sort.
     */
    private static final String INSERTION_SORT_METHOD2 = "    /**\n"
            + "     * Insertion sort. The bounds specify which part of the array is to be sorted.<br />\n"
            + "     * <br />\n" + "     * Algorithm of O( n² ). <br />\n"
            + "     * This version of insertion sort also allows for bounds to be put in to specify what part of the array will be sorted. <br />\n"
            + "     * The part of the array that lies between <b>left</b> and <b>right</b> is the only part that will be sorted.\n"
            + "     * \n" + "     * @param A\n" + "     *            an array of Comparable items.\n"
            + "     * @param Assoc\n" + "     *            The associative Array\n" + "     * @param left\n"
            + "     *            the left-most index of the subarray.\n" + "     * @param right\n"
            + "     *            the right-most index of the subarray.\n"
            + "     * @return A reference to the array that was sorted.\n" + "     */\n" + "    public static "
            + GENERIC_DECLARATION_KEY + " " + SORTED_TYPE_KEY + "[] insertionSort(" + SORTED_TYPE_KEY + "[] A, "
            + ASSOCIATED_TYPE_KEY + "[] Assoc, int left, int right) {\n"
            + "        for (int p = left + 1; p <= right; p++) {\n" + "            " + SORTED_TYPE_KEY
            + " tmp = A[p];\n" + "            " + ASSOCIATED_TYPE_KEY + " temp = Assoc[p];\n" + "            int j;\n"
            + "            for (j = p; j > left && tmp < A[j - 1]; j--) {\n" + "                A[j] = A[j - 1];\n"
            + "                Assoc[j] = Assoc[j - 1];\n" + "            }\n" + "            A[j] = tmp;\n"
            + "            Assoc[j] = temp;\n" + "        }\n" + "        return A;\n" + "    }\n\n";

    /**
     * String defining the feet of the {@link AssociativeSort} class.
     */
    private static final String CLASS_FEAT = "}";

    /**
     * Head of the {@link AssociativeSortTest} class.
     */
    private static final String TEST_CLASS_HEAD = "package org.aksw.simba.topicmodeling.commons.sort;\n\n"
            + "import java.util.Random;\n\n" + "import org.junit.Assert;\n" + "import org.junit.Test;\n\n" + "\n"
            + "/**\n" + " * This class contains JUnit tests for the {@link AssociativeSort} class.\n" + " * \n"
            + " * <b>Note</b> that this class has been generated automatically using the {@link AssociativeSortClassGenerator} class.\n"
            + " * Do never change the source code in this class because changes could be overwritten by the genrating class. Change the\n"
            + " * source code of the {@link AssociativeSortClassGenerator} class instead.\n" + " * \n"
            + " * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>\n" + " */\n"
            + "public class AssociativeSortTest {\n\n" + "    /**\n"
            + "     * Number of swaps which are done to get a random order of the array elements.\n" + "     */\n"
            + "    private static final int INITIALE_SWAPS = 5;\n\n" + "    /**\n"
            + "     * Used to get a random order of the array elements.\n" + "     */\n"
            + "    private Random rand = new Random();\n\n";

    /**
     * String defining the JUnit test method.
     */
    private static final String TEST_METHOD = "    @Test\n" + "    public void test" + TEST_METHOD_NAME_KEY
            + "Sort() {\n" + "        " + SORTED_TYPE_KEY + "[] keyArray = " + SORTED_TYPE_EXAMPLE_KEY + ";\n"
            + "        " + ASSOCIATED_TYPE_KEY + "[] associatedArray = " + ASSOCIATED_TYPE_EXAMPLE_KEY + ";\n"
            + "        // make some swaps\n" + "        for (int i = 0; i < INITIALE_SWAPS; ++i) {\n"
            + "            AssociativeSort.swap(keyArray, associatedArray, rand.nextInt(keyArray.length),\n"
            + "                    rand.nextInt(keyArray.length));\n" + "        }\n"
            + "        AssociativeSort.quickSort(keyArray, associatedArray);\n"
            + "        Assert.assertArrayEquals(new " + SORTED_TYPE_KEY + "[]" + SORTED_TYPE_EXAMPLE_KEY + ", keyArray"
            + ARRAY_EQUALS_DELTA_SORTED_ARRAY_KEY + ");\n" + "        Assert.assertArrayEquals(new "
            + ASSOCIATED_TYPE_KEY + "[]" + ASSOCIATED_TYPE_EXAMPLE_KEY + ", associatedArray"
            + ARRAY_EQUALS_DELTA_ASSOCIATED_ARRAY_KEY + ");\n" + "    }\n\n";

    /**
     * String defining the feet of the {@link AssociativeSortTest} class.
     */
    private static final String TEST_CLASS_FEAT = "}";
}
