package expression.exceptions;

import expression.*;

public class CheckedNegate extends Negate {
    public CheckedNegate(CommonExpression left) {
        super(left);
    }

    public int compute(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException(toString());
        }
        return super.compute(x);
    }
}