package expression.exceptions;

import expression.Divide;
import expression.CommonExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int compute(int x, int y) {
        if (y == 0) {
            throw new DivisionByZeroException(toString());
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException(toString());
        }
        return super.compute(x, y);
    }
}