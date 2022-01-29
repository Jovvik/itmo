package expression;

import expression.exceptions.UnknownVariableException;

public class Variable<T> implements TripleExpression<T> {
    private char name;

    public Variable(char name) {
        this.name = name;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        switch (name) {
            case 'x':
                return x;
            case 'y':
                return y;
            case 'z':
                return z;
            default:
                throw new UnknownVariableException(String.valueOf(name));
        }
    }
}