package expression;

import expression.exceptions.OverflowException;
import expression.type.Type;

public abstract class UnaryOperation<T> implements TripleExpression<T> {
    protected final TripleExpression<T> exp;
    protected final Type<T> type;

    public UnaryOperation(TripleExpression<T> exp, Type<T> type) {
        this.exp = exp;
        this.type = type;
    }

    protected abstract T compute(T first);

    public T evaluate(T x, T y, T z) {
        return compute(exp.evaluate(x, y, z));
    }
}