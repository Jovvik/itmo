package expression.exceptions;

public class InvalidArgumentException extends NumericException {
    public InvalidArgumentException(String operation) {
        super(operation);
    }
}