package expression;

import expression.type.Type;

public class Divide<T> extends BinaryOperation<T> {
    public Divide(TripleExpression<T> first, TripleExpression<T> second, Type<T> type) {
        super(first, second, type);
    }

    public T compute(T first, T second) {
        return type.divide(first, second);
    }
}