package expression.exceptions;

import expression.CommonExpression;
import expression.RightShift;

public class CheckedRightShift extends RightShift {
    public CheckedRightShift(CommonExpression left, CommonExpression right) {
        super(left, right);
    }
}