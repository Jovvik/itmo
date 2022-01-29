package expression;

public class Pow extends BinaryOperation {
    public Pow(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    protected String getSign() {
        return "**";
    }

    //  (cond (= 0 b) 1 (= 1 b) a (even? b) (power (* a a) (quot b 2)) (odd? b) (* a (power a (- b 1)))))
    public int compute(int x, int y) {
        if (y == 0) {
            return 1;
        }
        if (y == 1) {
            return x;
        }
        if (y % 2 == 0) {
            return compute(x * x, y / 2);
        } else {
            return x * compute(x, y - 1);
        }
    }

    protected int getPriority() {
        return 1;
    }

    protected boolean isOrderImportant() {
        return true;
    }
}