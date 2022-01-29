package expression;

import expression.exceptions.OverflowException;
import expression.type.Type;

public class Negate<T> extends UnaryOperation<T> {
    public Negate(TripleExpression<T> exp, Type<T> type) {
        super(exp, type);
    }

    protected T compute(T first) throws OverflowException {
        return type.negate(first);
    }
}