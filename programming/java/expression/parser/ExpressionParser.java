package expression.parser;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import expression.*;

enum Token {
    NUM, END, PLUS, MINUS, MUL, DIV, LEFTSHIFT, RIGHTSHIFT, REVERSE, DIGITS, LP, RP, VARNAME
}

public class ExpressionParser extends BaseParser {
    private final static Map<String, Token> stringToOperator = Map.ofEntries(
        Map.entry("+", Token.PLUS),
        Map.entry("-", Token.MINUS),
        Map.entry("*", Token.MUL),
        Map.entry("/", Token.DIV),
        Map.entry(">>", Token.RIGHTSHIFT),
        Map.entry("<<", Token.LEFTSHIFT),
        Map.entry("reverse", Token.REVERSE),
        Map.entry("digits", Token.DIGITS),
        Map.entry("\0", Token.END),
        Map.entry("(", Token.LP),
        Map.entry(")", Token.RP)
    );
    private final static EnumMap<Token, Integer> priorities = new EnumMap<>(Map.of(
        Token.LEFTSHIFT, 3,
        Token.RIGHTSHIFT, 3,
        Token.PLUS, 2,
        Token.MINUS, 2,
        Token.MUL, 1,
        Token.DIV, 1
    ));
    private final static int maxPriority = Collections.max(priorities.values());
    private final static Set<Character> allowedVariables = Set.of('x', 'y', 'z');

    private char lastVarName;
    private Token lastToken;

    public CommonExpression parse(String expression) {
        setSource(new StringSource(trim(expression)));
        nextChar();
        getToken();
        final CommonExpression result = parseExpression(maxPriority, false);
        if (lastToken != Token.END) {
            throw error("End of expression expected");
        }
        return result;
    }

    private CommonExpression parseExpression(int level, boolean get) {
        if (level == 0) {
            return parseBaseExpression(get);
        }
        CommonExpression result = parseExpression(level - 1, get);
        while (priorities.getOrDefault(lastToken, -1) == level) {
            result = CreateExpression(lastToken, result, parseExpression(level - 1, true));
        }
        return result;
    }

    private CommonExpression parseBaseExpression(boolean get) {
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
                if (between('0', '9')) {
                    getToken();
                    return new Const(parseInt(true));
                } else {
                    getToken();
                    return new Negate(parseBaseExpression(false));
                }
            case REVERSE:
                getToken();
                return new Reverse(parseBaseExpression(false));
            case DIGITS:
                getToken();
                return new Digits(parseBaseExpression(false));
            case LP:
                CommonExpression result = parseExpression(maxPriority, true);
                if (lastToken != Token.RP) {
                    throw error("Right parenthesis expected, got " + lastToken.name());
                }
                getToken();
                return result;
            default:
                throw error("Primary expression expected, got " + lastToken.name());
        }
    }

    private void getToken() {
        for (String op : stringToOperator.keySet()) {
            if (op.charAt(0) == ch) {
                expect(op, "Malformed " + op);
                lastToken = stringToOperator.get(op);
                return;
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

    private int parseInt(boolean negative) {
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
        } catch(NumberFormatException e) {
            throw error("Illegal constant: " + sb.toString());
        }
    }

    private CommonExpression CreateExpression(Token type, CommonExpression left, CommonExpression right) {
        //? Is this copypaste alright or should a Map with reflection be used?
        switch (type) {
            case MUL:
                return new Multiply(left, right);
            case DIV:
                return new Divide(left, right);
            case MINUS:
                return new Subtract(left, right);
            case PLUS:
                return new Add(left, right);
            case LEFTSHIFT:
                return new LeftShift(left, right);
            case RIGHTSHIFT:
                return new RightShift(left, right);
            default:
                throw error("Unsupported binary operator");
        }
    }

    private String trim(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}