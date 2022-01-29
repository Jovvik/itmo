package queue;

import java.util.function.Function;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    public Object element() {
        assert size != 0;
        return head.value;
    }

    public void enqueue(Object x) {
        Node newNode = new Node(x, null);
        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public Object dequeue() {
        Object retval = head.value;
        head = head.next;
        size--;
        return retval;
    }

    public void clear() {
        size = 0;
        tail = null;
        head = null;
    }

    private class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }

    protected Queue iterate(Function<Object, Object> f) {
        Node current = head;
        Queue resultQueue = new LinkedQueue();
        while (current != null) {
            operate(resultQueue, current.value, f);
            current = current.next;
        }
        return resultQueue;
    }
}