package expression.exceptions;

import expression.*;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int compute(int x, int y) {
        if (((y != 0) && (x * y / y != x)) || (x == Integer.MIN_VALUE && y == -1)
                || (y == Integer.MIN_VALUE && x == -1)) {
            throw new OverflowException(toString());
        }
        return super.compute(x, y);
    }
}