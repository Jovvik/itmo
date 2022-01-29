package expression.exceptions;

import expression.CommonExpression;
import expression.LeftShift;

public class CheckedLeftShift extends LeftShift {
    public CheckedLeftShift(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int compute(int x, int y) {
        if (x >> (Integer.BYTES * 8 - y) != 0) {
            throw new OverflowException(toString());
        }
        return super.compute(x, y);
    }
}