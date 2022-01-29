package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    protected abstract Queue iterate(Function<Object, Object> f);

    public Queue map(Function<Object, Object> f) {
        return iterate(f);
    }

    public Queue filter(Predicate<Object> f) {
        return iterate(x -> f.test(x) ? x : null);
    }

    protected final void operate(Queue queue, Object value, Function<Object, Object> f) {
        Object appliedObject = f.apply(value);
        if (appliedObject != null) {
            queue.enqueue(appliedObject);
        }
    }
}