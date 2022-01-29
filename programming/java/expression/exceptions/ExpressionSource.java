package expression.exceptions;

public interface ExpressionSource {
    boolean hasNext();
    char next();
    ParserException error(final String message);
    void savePos();
    void restorePos();
}