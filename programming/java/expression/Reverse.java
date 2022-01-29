package expression;

public class Reverse extends UnaryOperation {
    public Reverse(CommonExpression exp) {
        super(exp);
    }

    protected int compute(int n) {
        int result = 0;
        while (n != 0) {
            result *= 10;
            result += n % 10;
            n /= 10;
        }
        return result;
    }

    @Override
    protected String getOperatorString() {
        return "reverse";
    }
}