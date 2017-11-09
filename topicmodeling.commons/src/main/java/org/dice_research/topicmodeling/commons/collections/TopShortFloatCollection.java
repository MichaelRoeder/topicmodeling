package org.dice_research.topicmodeling.commons.collections;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * <p>
 * This container allows you to hold the <b>top n value-object-pairs</b>. If the
 * {@link #sortAscending} flag is set, the best values are the lowest of the
 * collection. If the flag is set to false, the highest values are used as best.
 * </p>
 * 
 * <p>
 * <b>Note</b> that all value-object-pairs with a value worse than the nth best
 * element are discarded.
 * </p>
 * 
 * <p>
 * <b>Note</b> that the {@link #values} array is sorted ascending or descending
 * relying on the value of the {@link #sortAscending} flag. The elements of the
 * {@link #objects} array are sorted associated to their values in the
 * {@link #values} array.
 * </p>
 * 
 * <p>
 * <b>Note</b> that this class has been generated using the class
 * {@link TopCollectionClassGenerator}. If changes to this class are necessary,
 * they should be made inside the generator class.
 * </p>
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 * 
 */
public class TopShortFloatCollection {

    /**
     * <p>
     * An array containing the n best values seen by this container.
     * </p>
     * 
     * <p>
     * <b>Note</b> that the array has the size with which this container has
     * been constructed. If you want to iterate through the best values known by
     * this container you should iterate from 0 to the {@link #size()} of the
     * container and not to the end of this array.
     * </p>
     */
    public short values[];

    /**
     * <p>
     * An array containing the objects associated to the n best values seen by
     * this container.
     * </p>
     * 
     * <p>
     * <b>Note</b> that the array has the size with which this container has
     * been constructed. If you want to iterate through the objects associated
     * to the best values known by this container you should iterate from 0 to
     * the {@link #size()} of the container and not to the end of this array.
     * </p>
     */
    public float objects[];

    /**
     * <p>
     * current size of this container.
     * </p>
     */
    private int size = 0;

    /**
     * <p>
     * Flag defining the order of the value elements in this container. If it is
     * true the values are ordered ascending and the container will keep only
     * the lowest values. If it is false the values are order descending and the
     * container will keep only the highest values.
     * </p>
     */
    private boolean sortAscending;

    /**
     * <p>
     * Creates a container which holds the n best short values and the objects
     * associated with them. Which short values are interpreted as the best is
     * defined by the given flag.
     * </p>
     * 
     * @param n
     *            The maximum number of elements which shall be stored by this
     *            container.
     * @param sortAscending
     *            If this flag is true the lowest short value will be
     *            interpreted as the best value. If it is false the highest
     *            short value is interpreted as the best value.
     */
    public TopShortFloatCollection(int n, boolean sortAscending) {
        values = new short[n];
        objects = new float[n];
        this.sortAscending = sortAscending;
    }

    /**
     * <p>
     * Adds the given value and the object associated with to the container if
     * the given value is inside the top n values.
     * </p>
     * 
     * @param value
     *            The value which defines if the given element is inside the top
     *            n.
     * @param object
     *            The object associated to the given value.
     * @return True if the added value is inside the current top values, else
     *         False.
     */
    public boolean add(short value, float object) {
        int position = findPosition(value);
        if (position < values.length) {
            if (position < size) {
                int newPosition = position + 1;
                int length = size == values.length ? values.length - newPosition : size - position;
                System.arraycopy(values, position, values, newPosition, length);
                System.arraycopy(objects, position, objects, newPosition, length);
            }
            // insert this object
            values[position] = value;
            objects[position] = object;
            if (size < values.length) {
                ++size;
            }
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Searches for the position of the given value inside the {@link #values}
     * array.
     * </p>
     * 
     * @param value
     *            The value for which the position inside the array should be
     *            found.
     * @return The position or the size of the {@link #values} array if the
     *         given value is not inside the top n values.
     */
    private int findPosition(short value) {
        // check if there are elements inside the array
        if (size > 0) {
            // check that this value is inside the array
            if (sortAscending ? value >= values[size - 1] : value <= values[size - 1]) {
                return size;
            } else {
                return findPosition(value, 0, size);
            }
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * Searches for the position of the given value inside a sub array of the
     * {@link #values} array.
     * </p>
     * 
     * <p>
     * This method implements a recursive binary search.
     * </p>
     * 
     * @param value
     *            The value for which the position inside the array should be
     *            found.
     * @param startPosition
     *            The index of the first element of the sub array.
     * @param endPosition
     *            The index of the last element of the sub array.
     * @return The position of this value inside the sub array.
     */
    private int findPosition(short value, int startPosition, int endPosition) {
        int dist = endPosition - startPosition;
        switch (dist) {
        case 1: {
            // if the startpos is 0 it hasn't been compared to the given value
            // (all other start positions would have been compared at this
            // point)
            if ((startPosition == 0) && (sortAscending ? value < values[0] : value > values[0])) {
                return 0;
            } else {
                return endPosition;
            }
        }
        case 2: {
            // if the startpos is 0 it hasn't been compared to the given value
            // (all other start positions would have been compared at this
            // point)
            if ((startPosition == 0) && (sortAscending ? value < values[0] : value > values[0])) {
                return 0;
            } else if (sortAscending ? value < values[startPosition + 1] : value > values[startPosition + 1]) {
                return startPosition + 1;
            } else {
                return endPosition;
            }
        }
        default: {
            // halve the sub array and decide in which one of the two parts the
            // search should be continued
            int middle = startPosition + ((endPosition - startPosition) / 2);
            if (sortAscending ? value < values[middle] : value > values[middle]) {
                return findPosition(value, startPosition, middle);
            } else {
                return findPosition(value, middle, endPosition);
            }
        }
        }
    }

    /**
     * <p>
     * Returns the array of objects associated to the values.
     * </p>
     * 
     * @return An array of objects associated to the values.
     */
    public float[] toArray() {
        return Arrays.copyOf(objects, size);
    }

    /**
     * <p>
     * Returns the array of the n best values.
     * </p>
     * 
     * <p>
     * <b>Note</b> that this is the original array and not a copy.
     * </p>
     * 
     * @return The array of the n best values.
     */
    public short[] getValues() {
        return values;
    }

    /**
     * <p>
     * Returns the array of objects associated to the best values.
     * </p>
     * 
     * <p>
     * <b>Note</b> that this is the original array and not a copy.
     * </p>
     * 
     * @return The array of objects associated to the values.
     */
    public float[] getObjects() {
        return objects;
    }

    /**
     * <p>
     * Returns the size of this container.
     * </p>
     * 
     * @return The size of this container.
     */
    public int size() {
        return size;
    }

    /**
     * <p>
     * Clears the container.
     * </p>
     */
    public void clear() {
        size = 0;
    }

    /**
     * <p>
     * Removes the value with the given index and its associated object from the
     * container.
     * </p>
     * 
     * @param index
     */
    public void remove(int index) {
        if (index < size) {
            System.arraycopy(objects, index + 1, objects, index, size - (index + 1));
            System.arraycopy(values, index + 1, values, index, size - (index + 1));
            --size;
        }
    }

    /**
     * <p>
     * Pops the first object (the one with the best value) from the Container.
     * In other words, removes and returns the first object of this container.
     * </p>
     * 
     * @return The first object of this container.
     * @throws NoSuchElementException
     *             - If this container is empty.
     */
    public float pop() {
        if (size > 0) {
            float first = objects[0];
            System.arraycopy(objects, 1, objects, 0, size - 1);
            System.arraycopy(values, 1, values, 0, size - 1);
            --size;
            return first;
        } else {
            throw new NoSuchElementException("Can't pop an element from an empty array.");
        }
    }
}
