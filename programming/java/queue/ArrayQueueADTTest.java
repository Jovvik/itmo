package queue;

public class ArrayQueueADTTest {
    public static void fill(ArrayQueueADT queue) {
        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.push(queue, i);
        }
        for (int i = 10; i < 19; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
    }

    public static void dumpFront(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(String.format("first element=%3s dequeued element=%3s size=%3s", ArrayQueueADT.element(queue), ArrayQueueADT.dequeue(queue), ArrayQueueADT.size(queue)));
        }
    }

    public static void dumpBack(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(String.format("last element=%3s removed element=%3s size=%3s", ArrayQueueADT.peek(queue), ArrayQueueADT.remove(queue), ArrayQueueADT.size(queue)));
        }
    }

    public static void main(String[] args) {
        System.out.println("ADT queue test");
        ArrayQueueADT queue = new ArrayQueueADT();
        fill(queue);
        dumpFront(queue);
        fill(queue);
        dumpBack(queue);
    }
}