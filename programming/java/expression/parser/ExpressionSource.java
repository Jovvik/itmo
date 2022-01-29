package expression.parser;

import expression.ExpressionException;

public interface ExpressionSource {
    boolean hasNext();
    char next();
    ExpressionException error(final String message);
}