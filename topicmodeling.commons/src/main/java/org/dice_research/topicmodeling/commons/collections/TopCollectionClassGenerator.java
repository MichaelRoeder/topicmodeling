package org.dice_research.topicmodeling.commons.collections;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * This class generates all TopXYCollection classes and their test classes.
 * Changes which should be made to these classes should be made inside this
 * class or the loaded resources.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 * 
 */
class TopCollectionClassGenerator {

    private static final String CLASS_NAME_PATTERN = "Top$VALUE$$OBJECT$Collection";

    private static final String PATH_TO_COLLECTION_CLASS_FILE = "src/main/java/org/dice_research/topicmodeling/commons/collections/$CLASS$.java";

    private static final String PATH_TO_COLLECTION_TEST_CLASS_FILE = "src/test/java/org/dice_research/topicmodeling/commons/collections/$CLASS$Test.java";

    private static final String CLASS_NAME_KEY = "$CLASS$";

    /**
     * Defining the String which will be replaced with the type of the sorted
     * array.
     */
    private static final String VALUE_TYPE_KEY = "$VALUE$";

    /**
     * Defining the String which will be replaced with the type of the
     * associated array.
     */
    private static final String ASSOCIATED_TYPE_KEY = "$OBJECT$";

    private static final String RANDOM_VALUE_CREATION_KEY = "$RANDOM_VALUE$";
    private static final String RANDOM_OBJECT_CREATION_KEY = "$RANDOM_OBJECT$";

    /**
     * Defining the String which will be replaced with the delta which is needed
     * to compare double or float values of the sorted array in the JUnit tests
     * (or an empty String if the type of the sorted array is not double or
     * float).
     */
    private static final String ARRAY_EQUALS_DELTA_VALUE_ARRAY_KEY = "$VALUE_DELTA$";

    /**
     * Defining the String which will be replaced with the delta which is needed
     * to compare double or float values of the associated array in the JUnit
     * tests (or an empty String if the type of the associated array is not
     * double or float).
     */
    private static final String ARRAY_EQUALS_DELTA_OBEJCT_ARRAY_KEY = "$OBJECT_DELTA$";

    private static final String CLASS_PATTERN_FILE = "ClassDefinitions/TopCollectionClassDefinition.java";
    private static final String TEST_CLASS_PATTERN_FILE = "ClassDefinitions/TopCollectionTestClassDefinition.java";
    private static final String GENERIC_CLASS_PATTERN_FILE = "ClassDefinitions/TopCollectionGenericClassDefinition.java";
    private static final String GENERIC_TEST_CLASS_PATTERN_FILE = "ClassDefinitions/TopCollectionGenericTestClassDefinition.java";

    /**
     * Defining the types the sorted array can have.
     */
    private static final String PRIMITIVE_SORT_TYPES[] = { "byte", "char", "short", "int", "long", "float", "double" };

    /**
     * Defining the types the associated array can have.
     */
    private static final String ASSOCIATED_TYPES[] = { "boolean", "byte", "char", "short", "int", "long", "float",
            "double" };

    /**
     * Starts the generation.
     * 
     * @param args
     */
    public static void main(String[] args) {
        TopCollectionClassGenerator generator = new TopCollectionClassGenerator();
        generator.writeTopCollectionClasses(PRIMITIVE_SORT_TYPES, ASSOCIATED_TYPES);
    }

    private void writeTopCollectionClasses(String primitiveSortTypes[], String associatedTypes[]) {
        String classString = loadClassDefintion(CLASS_PATTERN_FILE);
        String testClassString = loadClassDefintion(TEST_CLASS_PATTERN_FILE);
        String genericClassString = loadClassDefintion(GENERIC_CLASS_PATTERN_FILE);
        String genericTestClassString = loadClassDefintion(GENERIC_TEST_CLASS_PATTERN_FILE);
        if (classString == null) {
            System.err.println("Couldn't load TopCollection class definition. Aborting.");
            return;
        }
        try {
            writeTopCollectionClasses(primitiveSortTypes, associatedTypes, classString, testClassString,
                    genericClassString, genericTestClassString);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't write TopCollection classes. Aborting.");
            return;
        }
    }

    /**
     * Loads the class definition from file.
     * 
     * @param filename
     *            the filename containing the class definition.
     * @return
     */
    private String loadClassDefintion(String filename) {
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(filename);
            if (is == null) {
                System.err.println("Couldn't load TopCollection class definition (\"" + filename
                        + "\") from the resources. Returning null.");
                return null;
            }
            byte buffer[] = new byte[1024];
            StringBuilder classDefinitionBuilder = new StringBuilder();
            int length;
            length = is.read(buffer);
            while (length > 0) {
                classDefinitionBuilder.append(new String(buffer, 0, length));
                length = is.read(buffer);
            }
            return classDefinitionBuilder.toString();
        } catch (IOException e) {
            System.err.println("Exception while Loading TopCollection class definition. Returning null.");
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // nothing to do
                }
            }
        }
    }

    private void writeTopCollectionClasses(String primitiveSortTypes[], String associatedTypes[], String classString,
            String testClassString, String genericClassString, String genericTestClassString) throws IOException {
        for (int i = 0; i < primitiveSortTypes.length; i++) {
            for (int j = 0; j < associatedTypes.length; j++) {
                createClassFile(primitiveSortTypes[i], associatedTypes[j], classString);
                createTestClassFile(primitiveSortTypes[i], associatedTypes[j], testClassString);
            }
            createGenericClassFile(primitiveSortTypes[i], genericClassString);
            createGenericTestClassFile(primitiveSortTypes[i], genericTestClassString);
        }
    }

    /**
     * This method creates the class file of a collection with the given value
     * and associated data types.
     * 
     * @param sortedType
     *            The type of the elements which will be used for sorting.
     * @param associatedType
     *            The type of the associated elements.
     * @param classDefintion
     *            The definition of the class.
     * @throws IOException
     */
    private void createClassFile(String sortedType, String associatedType, String classDefintion) throws IOException {
        String associatedTypeName, sortedTypeName, adoptedClassDefinition;

        associatedTypeName = Character.toUpperCase(associatedType.charAt(0)) + associatedType.substring(1);
        sortedTypeName = Character.toUpperCase(sortedType.charAt(0)) + sortedType.substring(1);

        String className = CLASS_NAME_PATTERN.replace(VALUE_TYPE_KEY, sortedTypeName).replace(ASSOCIATED_TYPE_KEY,
                associatedTypeName);

        adoptedClassDefinition = classDefintion.replace(CLASS_NAME_KEY, className);
        adoptedClassDefinition = adoptedClassDefinition.replace(VALUE_TYPE_KEY, sortedType);
        adoptedClassDefinition = adoptedClassDefinition.replace(ASSOCIATED_TYPE_KEY, associatedType);

        PrintStream fout = new PrintStream(PATH_TO_COLLECTION_CLASS_FILE.replace(CLASS_NAME_KEY, className));
        fout.print(adoptedClassDefinition);
        fout.close();
    }

    /**
     * This method creates the test class of a collection with the given value
     * and associated data types.
     * 
     * @param sortedType
     *            The type of the elements which will be used for sorting.
     * @param associatedType
     *            The type of the associated elements.
     * @param testClassDefinition
     *            The definition of the test class.
     * @throws IOException
     */
    private void createTestClassFile(String sortedType, String associatedType, String testClassDefinition)
            throws IOException {
        String associatedTypeName, sortedTypeName, adoptedClassDefinition;

        sortedTypeName = Character.toUpperCase(sortedType.charAt(0)) + sortedType.substring(1);
        associatedTypeName = Character.toUpperCase(associatedType.charAt(0)) + associatedType.substring(1);

        // Create the class name
        String className = CLASS_NAME_PATTERN.replace(VALUE_TYPE_KEY, sortedTypeName).replace(ASSOCIATED_TYPE_KEY,
                associatedTypeName);

        adoptedClassDefinition = testClassDefinition.replace(CLASS_NAME_KEY, className);
        adoptedClassDefinition = adoptedClassDefinition.replace(VALUE_TYPE_KEY, sortedType);
        adoptedClassDefinition = adoptedClassDefinition.replace(ASSOCIATED_TYPE_KEY, associatedType);

        // add the random creation of data
        if (sortedType.equals("byte")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY,
                    "(byte)(rand.nextInt() >> 24)");
        } else if (sortedType.equals("char")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY, "(char) rand.nextInt()");
        } else if (sortedType.equals("short")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY,
                    "(short)(rand.nextInt() >> 16)");
        } else {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY, "rand.next"
                    + sortedTypeName + "()");
        }

        if (associatedType.equals("byte")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_OBJECT_CREATION_KEY,
                    "(byte)(rand.nextInt() >> 24)");
        } else if (associatedType.equals("char")) {
            adoptedClassDefinition = adoptedClassDefinition
                    .replace(RANDOM_OBJECT_CREATION_KEY, "(char) rand.nextInt()");
        } else if (associatedType.equals("short")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_OBJECT_CREATION_KEY,
                    "(short)(rand.nextInt() >> 16)");
        } else if (associatedType.equals("Integer")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_OBJECT_CREATION_KEY,
                    "new Integer(rand.nextInt())");
        } else {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_OBJECT_CREATION_KEY, "rand.next"
                    + associatedTypeName + "()");
        }

        // Add deltas if needed
        if (sortedType.equals("double") || sortedType.equals("float")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(ARRAY_EQUALS_DELTA_VALUE_ARRAY_KEY, ", 0");
        } else {
            adoptedClassDefinition = adoptedClassDefinition.replace(ARRAY_EQUALS_DELTA_VALUE_ARRAY_KEY, "");
        }
        if (associatedType.equals("double") || associatedType.equals("float")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(ARRAY_EQUALS_DELTA_OBEJCT_ARRAY_KEY, ", 0");
        } else if (associatedType.equals("boolean")) {
            // There is no equality check for boolean arrays
            adoptedClassDefinition = adoptedClassDefinition.replace(
                    "Assert.assertArrayEquals(objects, topCollection.getObjects()$OBJECT_DELTA$);",
                    "boolean collectionObjects[] = topCollection.getObjects();\n        "
                            + "for(int i = 0; i < objects.length; ++i) {\n            "
                            + "Assert.assertTrue(objects[i] == collectionObjects[i]);\n        }");
            // There is no equality check for boolean arrays
            adoptedClassDefinition = adoptedClassDefinition.replace(
                    "Assert.assertArrayEquals(objectsSubArray1, objectsSubArray2$OBJECT_DELTA$);",
                    "for(int i = 0; i < objectsSubArray1.length; ++i) {\n                "
                            + "Assert.assertTrue(objectsSubArray1[i] == objectsSubArray2[i]);\n            }");
        } else {
            adoptedClassDefinition = adoptedClassDefinition.replace(ARRAY_EQUALS_DELTA_OBEJCT_ARRAY_KEY, "");
        }

        File outputFile = new File(PATH_TO_COLLECTION_TEST_CLASS_FILE.replace(CLASS_NAME_KEY, className));
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        PrintStream fout = new PrintStream(PATH_TO_COLLECTION_TEST_CLASS_FILE.replace(CLASS_NAME_KEY, className));
        fout.print(adoptedClassDefinition);
        fout.close();
    }

    /**
     * This method creates the class file of a collection with a generic object
     * type.
     * 
     * @param sortedType
     *            The type of the elements which will be used for sorting.
     * @param genericClassString
     *            The definition of the class.
     * @throws IOException
     */
    private void createGenericClassFile(String sortedType, String genericClassString) throws IOException {
        String sortedTypeName, adoptedClassDefinition;

        sortedTypeName = Character.toUpperCase(sortedType.charAt(0)) + sortedType.substring(1);

        String className = CLASS_NAME_PATTERN.replace(VALUE_TYPE_KEY, sortedTypeName).replace(ASSOCIATED_TYPE_KEY,
                "Object");

        adoptedClassDefinition = genericClassString.replace(CLASS_NAME_KEY, className);
        adoptedClassDefinition = adoptedClassDefinition.replace(VALUE_TYPE_KEY, sortedType);

        PrintStream fout = new PrintStream(PATH_TO_COLLECTION_CLASS_FILE.replace(CLASS_NAME_KEY, className));
        fout.print(adoptedClassDefinition);
        fout.close();
    }

    /**
     * This method creates the test class for collections with a generic object
     * type.
     * 
     * @param sortedType
     *            The type of the elements which will be used for sorting.
     * @param genericTestClassString
     *            The definition of the test class.
     * @throws IOException
     */
    private void createGenericTestClassFile(String sortedType, String genericTestClassString) throws IOException {
        String sortedTypeName, adoptedClassDefinition;

        sortedTypeName = Character.toUpperCase(sortedType.charAt(0)) + sortedType.substring(1);

        // Create the class name
        String className = CLASS_NAME_PATTERN.replace(VALUE_TYPE_KEY, sortedTypeName).replace(ASSOCIATED_TYPE_KEY,
                "Object");

        adoptedClassDefinition = genericTestClassString.replace(CLASS_NAME_KEY, className);
        adoptedClassDefinition = adoptedClassDefinition.replace(VALUE_TYPE_KEY, sortedType);

        // add the random creation of data
        if (sortedType.equals("byte")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY,
                    "(byte)(rand.nextInt() >> 24)");
        } else if (sortedType.equals("char")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY, "(char) rand.nextInt()");
        } else if (sortedType.equals("short")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY,
                    "(short)(rand.nextInt() >> 16)");
        } else {
            adoptedClassDefinition = adoptedClassDefinition.replace(RANDOM_VALUE_CREATION_KEY, "rand.next"
                    + sortedTypeName + "()");
        }

        // Add deltas if needed
        if (sortedType.equals("double") || sortedType.equals("float")) {
            adoptedClassDefinition = adoptedClassDefinition.replace(ARRAY_EQUALS_DELTA_VALUE_ARRAY_KEY, ", 0");
        } else {
            adoptedClassDefinition = adoptedClassDefinition.replace(ARRAY_EQUALS_DELTA_VALUE_ARRAY_KEY, "");
        }

        PrintStream fout = new PrintStream(PATH_TO_COLLECTION_TEST_CLASS_FILE.replace(CLASS_NAME_KEY, className));
        fout.print(adoptedClassDefinition);
        fout.close();
    }
}
