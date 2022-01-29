package expression.exceptions;

import expression.ExpressionException;

public abstract class BaseParser implements Parser {
    protected ExpressionSource source;
    protected char ch;

    protected void setSource(ExpressionSource source) {
        this.source = source;
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected void expect(final char c) throws ParserException {
        if (ch != c) {
            throw error("Expected '" + c + "'', got '" + ch + "'");
        }
        nextChar();
    }

    protected boolean expect(final String value) {
        source.savePos();
        try {
            for (char c : value.toCharArray()) {
                expect(c);
            }
        } catch (ParserException e) {
            source.restorePos();
            nextChar();
            return false;
        }
        return true;
    }

    protected ParserException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }
}
