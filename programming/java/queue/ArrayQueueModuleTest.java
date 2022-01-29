package queue;

public class ArrayQueueModuleTest {
    public static void fill() {
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.push(i);
        }
        for (int i = 10; i < 19; i++) {
            ArrayQueueModule.enqueue(i);
        }
    }

    public static void dumpFront() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(String.format("first element=%3s dequeued element=%3s size=%3s",
                    ArrayQueueModule.element(), ArrayQueueModule.dequeue(), ArrayQueueModule.size()));
        }
    }

    public static void dumpBack() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(String.format("last element=%3s removed element=%3s size=%3s", ArrayQueueModule.peek(),
                    ArrayQueueModule.remove(), ArrayQueueModule.size()));
        }
    }

    public static void main(String[] args) {
        System.out.println("Module queue test");
        fill();
        dumpFront();
        fill();
        dumpBack();
    }
}