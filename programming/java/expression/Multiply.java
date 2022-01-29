package expression;

import expression.type.Type;

public class Multiply<T> extends BinaryOperation<T> {
    public Multiply(TripleExpression<T> first, TripleExpression<T> second, Type<T> type) {
        super(first, second, type);
    }

    public T compute(T first, T second) {
        return type.multiply(first, second);
    }
}