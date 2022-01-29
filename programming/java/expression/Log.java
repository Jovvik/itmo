package expression;

public class Log extends BinaryOperation {
    public Log(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    protected String getSign() {
        return "//";
    }

    public int compute(int x, int y) {
        return computeNoCheck(x, y);
    }

    public int computeNoCheck(int x, int y) {
        if (x >= y) {
            return 1 + computeNoCheck(x / y, y);
        } else {
            return 0;
        }
    }

    protected int getPriority() {
        return 1;
    }

    protected boolean isOrderImportant() {
        return true;
    }
}