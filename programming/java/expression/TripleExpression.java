package expression;

import expression.exceptions.NumericException;

public interface TripleExpression<T> {
    T evaluate(T x, T y, T z);
}
