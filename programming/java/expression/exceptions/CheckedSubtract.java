package expression.exceptions;

import expression.*;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int compute(int x, int y) {
        if ((y > 0 && Integer.MIN_VALUE + y > x) || (y < 0 && Integer.MAX_VALUE + y < x)) {
            throw new OverflowException(toString());
        }
        return super.compute(x, y);
    }
}