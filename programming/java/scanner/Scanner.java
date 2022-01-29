import java.util.function.Function;
import java.io.*;
import java.util.NoSuchElementException;

public class Scanner {
    private static final int BUFFER_SIZE = 512;

    private BufferedReader reader;
    private StringBuilder next;
    private int nextInt;
    private int len;
    private int pos;
    private char[] buffer = new char[BUFFER_SIZE];

    public Scanner(String s) {
        this(new StringReader(s));
    }

    public Scanner(InputStream s) {
        this(new InputStreamReader(s));
    }

    public Scanner(BufferedReader r) {
        reader = r;
        pos = 0;
        len = 0;
        next = new StringBuilder();
    }

    public Scanner(Reader r) {
        this(new BufferedReader(r));
    }

    public boolean hasNext() throws IOException {
        if (next.length() == 0) {
            skipWhiteSpace();
            // buildToken(new StringBuilder(), Character::isWhitespace);
            // pos--;
            buildToken(next, (c) -> !Character.isWhitespace(c));
        }
        return next.length() > 0;
    }

    public boolean hasNextInt() throws IOException {
        if (!hasNext()) {
            return false;
        }
        try {
            nextInt = Integer.parseInt(next.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean hasNextLine() throws IOException {
        boolean res = true;
        if (nextChar() == (char) 0) {
            res = false;
        }
        pos--;
        return res;
    }

    public String nextLine() throws IOException {
        if (!hasNextLine()) {
            throw new NoSuchElementException();
        }
        StringBuilder nextLine = new StringBuilder();
        buildToken(nextLine, (c) -> c != '\n');
        return nextLine.toString();
    }

    public int nextInt() throws IOException {
        if (!hasNextInt()) {
            throw new NoSuchElementException();
        }
        next.setLength(0);
        return nextInt;
    }

    public String next() throws IOException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String res = next.toString();
        next.setLength(0);
        return res;
    }

    private void buildToken(StringBuilder target, Function<Character, Boolean> isNeeded) throws IOException {
        char currChar = nextChar();
        while (isNeeded.apply(currChar) && (int) currChar != 0) {
            target.append(currChar);
            currChar = nextChar();
        }
    }

    private char nextChar() throws IOException {
        if (pos >= len) {
            readBuffer();
        }
        if (len == -1) {
            return (char) 0;
        }
        return buffer[pos++];
    }

    private void skipWhiteSpace() throws IOException {
        char currChar;
        do {
            currChar = nextChar();
        } while (Character.isWhitespace(currChar));
        pos--;
    }

    private void readBuffer() throws IOException {
        do {
            len = reader.read(buffer);
        } while (len == 0);
        pos = 0;
    }

    public void close() throws IOException {
        reader.close();
    }
}