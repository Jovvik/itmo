package expression.exceptions;

public class DivisionByZeroException extends NumericException {
    public DivisionByZeroException(final String operation) {
        super("Division by zero in " + operation);
    }
}