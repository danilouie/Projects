import java.util.List;
import java.util.ArrayList;

public class LinkedListDeque<T> implements Deque<T> {

    private class Node { //Node Class
        T data;
        Node prev;
        Node next;
    }

    private Node sentinelNode;
    private int size;

    public LinkedListDeque() { // Constructor
        this.sentinelNode = new Node();
        sentinelNode.prev = sentinelNode;
        sentinelNode.next = sentinelNode;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node temp = new Node();
        temp.data = x;
        Node oldNext = sentinelNode.next;
        sentinelNode.next = temp;
        temp.prev = sentinelNode;
        temp.next = oldNext;
        oldNext.prev = temp;
        this.size += 1;
        if (this.size == 1) {
            sentinelNode.prev = temp;
        }
    }

    @Override
    public void addLast(T x) {
        Node temp = new Node();
        temp.data = x;
        Node oldPrev = sentinelNode.prev;
        sentinelNode.prev = temp;
        temp.next = sentinelNode;
        temp.prev = oldPrev;
        oldPrev.next = temp;
        this.size += 1;
        if (this.size == 1) {
            sentinelNode.next = temp;
        }
    }

    @Override public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node current = sentinelNode.next;

        while (current != sentinelNode) {
            returnList.add(current.data);
            current = current.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node newFirst = sentinelNode.next.next;
        Node oldFirst = sentinelNode.next;
        oldFirst.prev = null;
        oldFirst.next = null;
        sentinelNode.next = newFirst;
        newFirst.prev = sentinelNode;
        size--;
        return oldFirst.data;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node newLast = sentinelNode.prev.prev;
        Node oldLast = sentinelNode.prev;
        oldLast.prev = null;
        oldLast.next = null;
        sentinelNode.prev = newLast;
        newLast.next = sentinelNode;
        size--;
        return oldLast.data;
    }

    @Override
    public T get(int index) {
        Node value = sentinelNode.next;
        int count = 0;

        if (index >= size || index < 0) {
            return null;
        }
        while (count != index) {
            value = value.next;
            count++;
        }
        return value.data;
    }

    private T getRecursiveHelper(int index, Node node) {
        if (index == 0) {
            return node.data;
        } else {
            return getRecursiveHelper(index - 1, node.next);
        }
    }

    @Override
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return getRecursiveHelper(index, sentinelNode.next);
    }
}
