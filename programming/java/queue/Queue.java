package queue;

import java.util.function.Function;
import java.util.function.Predicate;

// queue <-> Q[1..size]
// Inv: (∀ i ∈ [1, size] : Q[i] != null) ∧ size >= 0
public interface Queue {
    // Pre: size != 0
    // Post: Q' = Q ∧ R = Q[1]
    public Object element();

    // Pre: x != null
    // Post: Q' = [Q_1...Q_size, x]
    public void enqueue(Object x);

    // Pre: size != 0
    // Post: Q' = [Q_2...Q_size] ∧ R = Q[1]
    public Object dequeue();
    
    // Post:R = size ∧ Q' = Q
    public int size();

    // Post: size' = 0
    public void clear();

    // Post: R = (size == 0) ∧ Q' = Q
    public boolean isEmpty();

    // Post: (∀ x = R_1..R_size : predicate(x) ∧ Q.contains(x) ∧ сохранен порядок)
    public Queue filter(Predicate<Object> predicate);

    // Post: R = [f(Q_1)...f(Q_size)]
    public Queue map(Function<Object, Object> f);
}