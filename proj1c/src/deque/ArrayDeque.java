package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int currentIndex;

        public ArrayDequeIterator() {
            currentIndex = 0;
        }

        public boolean hasNext() {
            return currentIndex < size;
        }

        public T next() {
            T returnItem = get(currentIndex);
            currentIndex++;
            return returnItem;
        }
    }

    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];

        for (int i = 0; i < size(); i++) {
            newArray[i] = get(i);
        }
        items = newArray;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = x;
        size++;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        size++;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast++;
        }
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            returnList.add(get(i));
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    public static final int MIN_CAPACITY = 16;

    private void resizeDownHelper(int capacity) {
        if (capacity >= MIN_CAPACITY) {
            if (size < 0.25 * capacity) {
                resize(size * 3);
            }
        }
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        resizeDownHelper(items.length);
        T first = get(0);
        nextFirst = (nextFirst + 1) % items.length;
        size--;
        return first;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        resizeDownHelper(items.length);
        T last = get(size - 1);
        nextLast = (nextLast - 1 + items.length) % items.length;
        size--;
        return last;
    }

    private T looperHelper(int index) {
        int newIndex = index - items.length;
        return items[newIndex];
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        int newIndex = nextFirst + 1 + index;
        if (newIndex >= items.length) {
            return looperHelper(newIndex);
        }
        return items[newIndex];
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Deque otherDeque) {
            if (this.size != otherDeque.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!this.get(i).equals(otherDeque.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return toList().toString();
    }
}
