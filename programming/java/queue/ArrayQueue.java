package queue;

// экземпляр ArrayQueue представим как Q[1..size]
// queue1 = queue2 <=> Q1 = [Q2_1..Q2_size]
// Inv: (∀ i ∈ [1, size] : Q[i] != null) ∧ size >= 0
public class ArrayQueue {
    private int start = 0;
    private int size = 0;
    private static final int START_SIZE = 2;
    private Object[] elements = new Object[START_SIZE];

    // Pre: size != 0
    // Post: Q' = Q ∧ R = Q[1]
    public Object element() {
        assert size != 0;
        return elements[start];
    }

    // Pre: x != null
    // Post: Q' = [Q_1...Q_size, x]
    public void enqueue(Object x) {
        assert x != null;

        ensureCapacity();
        size++;
        elements[end()] = x;
    }

    // Pre: size != 0
    // Post: Q' = [Q_2...Q_size] ∧ R = Q[1]
    public Object dequeue() {
        assert size != 0;

        Object value = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        size--;
        return value;
    }

    // Pre: size != 0
    // Post: Q' = [Q_1...Q_(size-1)] ∧ R = Q_size
    public Object remove() {
        assert size != 0;

        Object value = elements[end()];
        elements[end()] = null;
        size--;
        return value;
    }

    // Pre: x != null
    // Post: Q' = [x, Q_1...Q_size]
    public void push(Object x) {
        assert x != null;

        ensureCapacity();
        size++;
        start = (start - 1 + elements.length) % elements.length;
        elements[start] = x;
    }

    // Pre: size != 0
    // Post: Q' = Q ∧ R = Q_(size-1)
    public Object peek() {
        return elements[end()];
    }

    // Post : R = size ∧ Q' = Q
    public int size() {
        return size;
    }

    // Post : R = (size == 0) ∧ Q' = Q
    public boolean isEmpty() {
        return size == 0;
    }

    // Post: R = (start + size - 1) % elements.length ∧ Q' = Q
    private int end() {
        return (start + size - 1) % elements.length;
    }

    // Post: elements'.length > capacity ∧ Q' = Q
    private void ensureCapacity() {
        if (size >= elements.length) {
            Object[] newElements = new Object[size * 2];
            System.arraycopy(elements, start, newElements, 0, size - start);
            System.arraycopy(elements, 0, newElements, size - start, end() + 1);
            elements = newElements;
            start = 0;
        }
    }

    // Post: size' = 0
    public void clear() {
        elements = new Object[START_SIZE];
        start = 0;
        size = 0;
    }
}