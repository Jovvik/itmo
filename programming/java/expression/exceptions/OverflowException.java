package expression.exceptions;

public class OverflowException extends NumericException {
    public OverflowException(final String operation) {
        super("Overflow in " + operation);
    }
}