package expression;

import expression.type.Type;

public class Add<T> extends BinaryOperation<T> {
    public Add(TripleExpression<T> first, TripleExpression<T> second, Type<T> type) {
        super(first, second, type);
    }

    public T compute(T first, T second) {
        return type.add(first, second);
    }
}