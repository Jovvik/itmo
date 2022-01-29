package expression;

public abstract class AbstractOperation implements CommonExpression {
    protected CommonExpression left;
    protected CommonExpression right;
    
    public AbstractOperation(CommonExpression left, CommonExpression right) {
        this.left = left;
        this.right = right;
    }

    protected abstract String getSign();
    protected abstract int getPriority();
    protected abstract boolean isOrderImportant(); // in the same priority level
    protected abstract int compute(int x, int y);

    public int evaluate(int x) {
        return compute(left.evaluate(x), right.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return compute(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    public boolean equals(Object other) {
        return (other instanceof AbstractOperation
                && ((AbstractOperation) other).left.equals(this.left)
                && ((AbstractOperation) other).right.equals(this.right)
                && other.getClass().equals(this.getClass()));
    }

    public int hashCode() {
        return (left.hashCode() * 17 + right.hashCode()) * 239 + getClass().hashCode();
    }

    private boolean priorityParentheses(Expression a) {
        return (((a instanceof AbstractOperation) &&
            ((AbstractOperation) a).getPriority() < this.getPriority()));
    }

    private boolean orderParentheses(Expression a) {
        if (!(a instanceof AbstractOperation)) {
            return false;
        }
        AbstractOperation binA = (AbstractOperation) a;
        return (binA.isOrderImportant() || this.isOrderImportant()) &&
            binA.getPriority() <= this.getPriority();
    }

    private String applyPriority(Expression exp, boolean priority) {
        return (priority ? "(" : "") + exp.toMiniString() + (priority ? ")" : "");
    }

    public String toMiniString() {
        return applyPriority(left, priorityParentheses(left)) + " " + getSign() +
                " " + applyPriority(right, priorityParentheses(right) || orderParentheses(right));
    }

    public String toString() {
        return "(" + left + " " + getSign() + " " + right + ")"; 
    }
}