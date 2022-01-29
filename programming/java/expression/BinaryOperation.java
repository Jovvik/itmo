package expression;

import expression.exceptions.NumericException;
import expression.type.Type;

public abstract class BinaryOperation<T> implements TripleExpression<T> {
    protected final TripleExpression<T> left;
    protected final TripleExpression<T> right;
    protected final Type<T> type;
    
    public BinaryOperation(TripleExpression<T> left, TripleExpression<T> right, Type<T> type) {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    protected abstract T compute(T first, T second);

    public T evaluate(T x, T y, T z) {
        return compute(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}