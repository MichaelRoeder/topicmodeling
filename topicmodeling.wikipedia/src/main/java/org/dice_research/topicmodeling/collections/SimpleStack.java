package org.dice_research.topicmodeling.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SimpleStack<E> implements Collection<E> {

    private List<E> stack;

    public SimpleStack() {
        stack = new ArrayList<E>();
    }

    public SimpleStack(int initSize) {
        stack = new ArrayList<E>(initSize);
    }

    public SimpleStack(Collection<? extends E> collection) {
        stack = new ArrayList<E>(collection);
    }

    @Override
    public boolean add(E e) {
        return stack.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return stack.addAll(collection);
    }

    @Override
    public void clear() {
        stack.clear();
    }

    @Override
    public boolean contains(Object o) {
        return stack.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return stack.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return new StackIterator<E>(stack);
    }

    @Override
    public boolean remove(Object arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public Object[] toArray() {
        return stack.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return stack.toArray(array);
    }

    public E get(int index) {
        if ((index < 0) || (index >= stack.size())) {
            throw new IndexOutOfBoundsException("This SimpleStack only has "
                    + stack.size()
                    + " elements but the element with the index " + index
                    + " has been requested.");
        }
        return stack.get(stack.size() - (index + 1));
    }

    public E get() {
        if (stack.size() == 0) {
            throw new IndexOutOfBoundsException(
                    "This SimpleStack has no more elements.");
        } else {
            return stack.get(stack.size() - 1);
        }
    }

    public E getAndRemove() {
        if (stack.size() == 0) {
            throw new IndexOutOfBoundsException(
                    "This SimpleStack has no more elements.");
        } else {
            E element = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);
            return element;
        }
    }

    public void remove() {
        if (stack.size() == 0) {
            throw new IndexOutOfBoundsException(
                    "This SimpleStack has no more elements.");
        } else {
            stack.remove(stack.size() - 1);
        }
    }

    private static class StackIterator<E> implements Iterator<E> {
        private int id;

        private List<E> stack;

        public StackIterator(List<E> stack) {
            this.stack = stack;
            this.id = stack.size();
        }

        @Override
        public boolean hasNext() {
            return (id > 0) && (id <= stack.size());
        }

        @Override
        public E next() {
            --id;
            if ((id >= 0) && (id < stack.size())) {
                return stack.get(id);
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return "SimpleStack(" + stack.toString() + ")";
    }
}
