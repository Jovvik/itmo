package expression;

import java.util.function.BiFunction;

public abstract class Operator implements Expression {
    Expression left;
    Expression right;
    public Operator(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    abstract BiFunction<Expression, Expression, Integer> getOperation();
}