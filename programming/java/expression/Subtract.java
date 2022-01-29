package expression;

import expression.type.Type;

public class Subtract<T> extends BinaryOperation<T> {
    public Subtract(TripleExpression<T> first, TripleExpression<T> second, Type<T> type) {
        super(first, second, type);
    }

    public T compute(T first, T second) {
        return type.subtract(first, second);
    }
}