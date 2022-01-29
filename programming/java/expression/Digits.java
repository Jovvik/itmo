package expression;

public class Digits extends UnaryOperation {
    public Digits(CommonExpression exp) {
        super(exp);
    }

    protected int compute(int n) {
        return String.valueOf(n).chars().filter(x -> x != '-').map(Character::getNumericValue).sum();
    }

    protected String getOperatorString() {
        return "digits";
    }
}