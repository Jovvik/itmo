package expression.exceptions;

public class StringSource implements ExpressionSource {
    private final int ERROR_PREVIEW_SIZE = 5;
    private final String data;
    private int pos;
    private int savedPos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public ParserException error(final String message) {
        int leftEdge = Math.max(pos - ERROR_PREVIEW_SIZE, 0);
        int rightEdge = Math.min(pos + ERROR_PREVIEW_SIZE, data.length());
        return new ParserException("Error at char #" + (pos - 1) + ": " + (leftEdge == 0 ? "" : "…")
                + data.substring(leftEdge, rightEdge) + (rightEdge == data.length() ? "" : "…") + " : " + message);
    }

    public void savePos() {
        savedPos = pos;
    }

    public void restorePos() {
        pos = savedPos - 1;
    }
}
