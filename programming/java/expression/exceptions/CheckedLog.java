package expression.exceptions;

import expression.*;

public class CheckedLog extends Log {
    public CheckedLog(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int compute(int x, int y) {
        if (x <= 0 || y == 1) {
            throw new InvalidArgumentException(toString());
        }
        if (y <= 0) {
            throw new DivisionByZeroException(toString());
        }
        return super.compute(x, y);
    }
}