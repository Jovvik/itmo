package queue;

// экземпляр ArrayQueueADT представим как Q[1..size]
// queue1 = queue2 <=> Q1 = [Q2_1..Q2_size]
// Inv: (∀ i ∈ [1, size] : Q[i] != null) ∧ size >= 0
public class ArrayQueueADT {
    private int start = 0;
    private int size = 0;
    private static final int START_SIZE = 2;
    private Object[] elements = new Object[START_SIZE];

    // Pre: queue != null ∧ queue.size != 0
    // Post: Q' = Q ∧ R = Q[1]
    public static Object element(ArrayQueueADT queue) {
        assert queue.size != 0;
        return queue.elements[queue.start];
    }

    // Pre: queue != null ∧ x != null
    // Post: Q' = [Q_1...Q_size, x]
    public static void enqueue(ArrayQueueADT queue, Object x) {
        ensureCapacity(queue, queue.size);
        queue.size++;
        queue.elements[end(queue)] = x;
    }

    // Pre: queue != null ∧ queue.size != 0
    // Post: Q' = [Q_2...Q_size] ∧ R = Q[1]
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size != 0;
        Object value = queue.elements[queue.start];
        queue.elements[queue.start] = null;
        queue.start = (queue.start + 1) % queue.elements.length;
        queue.size--;
        return value;
    }

    // Pre: queue != null ∧ size != 0
    // Post: Q' = [Q_1...Q_(size-1)] ∧ R = Q_size
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size != 0;
        Object value = queue.elements[end(queue)];
        queue.elements[end(queue)] = null;
        queue.size--;
        return value;
    }

    // Pre: queue != null ∧ x != null
    // Post: Q' = [x, Q_1...Q_size]
    public static void push(ArrayQueueADT queue, Object x) {
        ensureCapacity(queue, queue.size);
        queue.start = (queue.start - 1 + queue.elements.length) % queue.elements.length;
        queue.elements[queue.start] = x;
        queue.size++;
    }

    // Pre: queue != null ∧ queue.size != 0
    // Post: Q' = Q ∧ R = Q_(size-1)
    public static Object peek(ArrayQueueADT queue) {
        return queue.elements[end(queue)];
    }

    // Pre: queue != null
    // Post: R = size ∧ Q' = Q
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: queue != null
    // Post: R = (size == 0) ∧ Q' = Q
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: queue != null
    // Post: R = (start + size - 1) % elements.length ∧ Q' = Q
    private static int end(ArrayQueueADT queue) {
        return (queue.start + queue.size - 1) % queue.elements.length;
    }

    // Pre: queue != null
    // Post: elements'.length > capacity ∧ Q' = Q
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity >= queue.elements.length) {
            Object[] newElements = new Object[queue.size * 2];
            System.arraycopy(queue.elements, queue.start, newElements, 0, queue.size - queue.start);
            System.arraycopy(queue.elements, 0, newElements, queue.size - queue.start, end(queue) + 1);
            queue.elements = newElements;
            queue.start = 0;
        }
    }

    // Pre: queue != null
    // Post: size' = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[START_SIZE];
        queue.start = 0;
        queue.size = 0;
    }
}