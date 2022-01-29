package queue;

public class ArrayQueueTest {
    public static void fill(ArrayQueue queue) {
        for (int i = 0; i < 10; i++) {
            queue.push(i);
        }
        for (int i = 10; i < 19; i++) {
            queue.enqueue(i);
        }
    }

    public static void dumpFront(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(String.format("first element=%3s dequeued element=%3s size=%3s", queue.element(), queue.dequeue(), queue.size()));
        }
    }

    public static void dumpBack(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(String.format("last element=%3s removed element=%3s size=%3s", queue.peek(), queue.remove(), queue.size()));
        }
    }

    public static void main(String[] args) {
        System.out.println("Class queue test");
        ArrayQueue queue = new ArrayQueue();
        fill(queue);
        dumpFront(queue);
        fill(queue);
        dumpBack(queue);
    }
}