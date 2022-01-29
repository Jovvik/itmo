package expression.exceptions;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import expression.*;

public class ExpressionParser extends BaseParser {
    enum Token {
        NUM, END, PLUS, MINUS, MUL, DIV, LEFTSHIFT, RIGHTSHIFT, REVERSE, DIGITS, LP, RP, VARNAME, POW, LOG
    }

    private final static Map<String, Token> stringToOperator = new LinkedHashMap<>();
    static {
        stringToOperator.put("//", Token.LOG);
        stringToOperator.put("**", Token.POW);
        stringToOperator.put("+", Token.PLUS);
        stringToOperator.put("-", Token.MINUS);
        stringToOperator.put("*", Token.MUL);
        stringToOperator.put("/", Token.DIV);
        stringToOperator.put(">>", Token.RIGHTSHIFT);
        stringToOperator.put("<<", Token.LEFTSHIFT);
        stringToOperator.put("reverse", Token.REVERSE);
        stringToOperator.put("digits", Token.DIGITS);
        stringToOperator.put("\0", Token.END);
        stringToOperator.put("(", Token.LP);
        stringToOperator.put(")", Token.RP);
    }
    private final static EnumMap<Token, Integer> priorities = new EnumMap<>(Map.of(
        Token.LOG, 1,
        Token.POW, 1,
        Token.LEFTSHIFT, 4,
        Token.RIGHTSHIFT, 4,
        Token.PLUS, 3,
        Token.MINUS, 3,
        Token.MUL, 2,
        Token.DIV, 2
    ));
    private final static int maxPriority = Collections.max(priorities.values());
    private final static Set<Character> allowedVariables = Set.of('x', 'y', 'z');

    private char lastVarName;
    private Token lastToken;

    public CommonExpression parse(String expression) throws ParserException {
        setSource(new StringSource(expression));
        nextChar();
        skipWhitespace();
        getToken();
        final CommonExpression result = parseExpression(maxPriority, false);
        if (lastToken != Token.END) {
            throw error("End of expression expected, got " + lastToken.name());
        }
        return result;
    }

    private CommonExpression parseExpression(int level, boolean get) throws ParserException {
        if (level == 0) {
            return parseBaseExpression(get);
        }
        CommonExpression result = parseExpression(level - 1, get);
        while (priorities.getOrDefault(lastToken, -1) == level) {
            result = CreateExpression(lastToken, result, parseExpression(level - 1, true));
        }
        return result;
    }

    private CommonExpression parseBaseExpression(boolean get) throws ParserException {
        if (get) {
            getToken();
        }

        switch (lastToken) {
        case NUM:
            getToken();
            return new Const(parseInt(false));
        case VARNAME:
            getToken();
            return new Variable(Character.toString(lastVarName));
        case MINUS:
            skipWhitespace();
            if (between('0', '9')) {
                getToken();
                return new Const(parseInt(true));
            } else {
                getToken();
                return new CheckedNegate(parseBaseExpression(false));
            }
        case REVERSE:
            getToken();
            return new Reverse(parseBaseExpression(false));
        case DIGITS:
            getToken();
            return new Digits(parseBaseExpression(false));
        case LP:
            getToken();
            CommonExpression result = parseExpression(maxPriority, false);
            if (lastToken != Token.RP) {
                throw error("Right parenthesis expected, got " + lastToken.name());
            }
            getToken();
            return result;
        default:
            throw error("Primary expression expected, got " + lastToken.name());
        }
    }

    private void getToken() throws ParserException {
        skipWhitespace();
        for (String op : stringToOperator.keySet()) {
            if (op.charAt(0) == ch) {
                // System.err.println(op + "passed");
                if (expect(op)) {
                    lastToken = stringToOperator.get(op);
                    return;
                }
            }
        }
        if (allowedVariables.contains(ch)) {
            lastVarName = ch;
            nextChar();
            lastToken = Token.VARNAME;
            return;
        }
        if (between('0', '9')) {
            lastToken = Token.NUM;
            return;
        } else {
            throw error("Unsupported variable name or operator");
        }
    }

    private int parseInt(boolean negative) throws ParserException {
        StringBuilder sb = new StringBuilder();
        while (between('0', '9')) {
            sb.append(ch);
            nextChar();
        }
        if (negative) {
            sb.insert(0, '-');
        }
        getToken();
        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            throw new OverflowException(sb.toString());
        }
    }

    private CommonExpression CreateExpression(Token type, CommonExpression left, CommonExpression right) throws ParserException {
        // ? Is this copypaste alright or should a Map with reflection be used?
        switch (type) {
        case MUL:
            return new CheckedMultiply(left, right);
        case DIV:
            return new CheckedDivide(left, right);
        case MINUS:
            return new CheckedSubtract(left, right);
        case PLUS:
            return new CheckedAdd(left, right);
        case POW:
            return new CheckedPow(left, right);
        case LOG:
            return new CheckedLog(left, right);
        case LEFTSHIFT:
            return new CheckedLeftShift(left, right);
        case RIGHTSHIFT:
            return new CheckedRightShift(left, right);
        default:
            throw error("Unsupported binary operator: " + type.name());
        }
    }
}