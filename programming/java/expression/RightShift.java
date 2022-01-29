package expression;

public class RightShift extends BinaryOperation {
    public RightShift(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    protected String getSign() {
        return ">>";
    }

    public int compute(int x, int y) {
        return x >> y;
    }

    protected int getPriority() {
        return 1;
    }

    protected boolean isOrderImportant() {
        return true;
    }
}