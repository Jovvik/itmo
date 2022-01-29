package expression.exceptions;

import expression.*;

public class CheckedPow extends Pow {
    public CheckedPow(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int compute(int x, int y) {
        if (y < 0 || x == 0 && y == 0) {
            throw new InvalidArgumentException(toString());
        }
        if (y == 0) {
            return 1;
        }
        if (y == 1) {
            return x;
        }
        if (y % 2 == 0) {
            return compute(CheckMul(x, x), y / 2);
        } else {
            return CheckMul(x, compute(x, y - 1));
        }
    }

    private int CheckMul(int x, int y) {
        if (((y != 0) && (x * y / y != x)) || (x == Integer.MIN_VALUE && y == -1)
                || (y == Integer.MIN_VALUE && x == -1)) {
            throw new OverflowException(toString());
        }
        return x * y;
    }
}