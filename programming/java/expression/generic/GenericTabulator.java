package expression.generic;

import java.util.Map;

import expression.ExpressionException;
import expression.TripleExpression;
import expression.parser.ExpressionParser;
import expression.type.*;

public class GenericTabulator implements Tabulator {

    private static final Map<String, Type<?>> TYPES = Map.of(
        "i", new CheckedIntegerType(),
        "d", new DoubleType(),
        "bi", new BigIntegerType()
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ExpressionException {
        Type<?> type = TYPES.get(mode);

        return tabulateImpl(type, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] tabulateImpl(Type<T> type, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ExpressionException {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        ExpressionParser<T> parser = new ExpressionParser<>(type);
        TripleExpression<T> parsedExpression = parser.parse(expression);

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i][j][k] = parsedExpression.evaluate(type.castInt(i), type.castInt(j), type.castInt(k));
                    } catch (ArithmeticException ignored) {
                    }
                }
            }
        }
        return result;
    }

}