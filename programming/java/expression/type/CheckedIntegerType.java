package expression.type;

public class CheckedIntegerType extends IntegerType {
    @Override
	public Integer add(Integer a, Integer b) {
		checkAdd(a, b);
		return super.add(a, b);
	}
	
	@Override
	public Integer subtract(Integer a, Integer b) {
		checkSubtract(a, b);
		return super.subtract(a, b);
	}
	
	@Override
	public Integer multiply(Integer a, Integer b) {
		checkMultiply(a, b);
		return super.multiply(a, b);
	}
	
	@Override
	public Integer divide(Integer a, Integer b) {
		checkDivide(a, b);
		return super.divide(a, b);
	}
	
	@Override
	public Integer negate(Integer a) {
		checkNegate(a);
		return super.negate(a);
	}
	
	private static void checkAdd(Integer x, Integer y) {
		if (x > 0 && Integer.MAX_VALUE - x < y || x < 0 && Integer.MIN_VALUE - x > y) {
			throw new ArithmeticException("Integer overflow");
		}
	}
	
	private void checkSubtract(Integer x, Integer y) {
		if ((y > 0 && Integer.MIN_VALUE + y > x) || (y < 0 && Integer.MAX_VALUE + y < x)) {
			throw new ArithmeticException("Integer overflow");
		}
	}
	
	private void checkMultiply(Integer x, Integer y) {
        if (((y != 0) && (x * y / y != x)) || (x == Integer.MIN_VALUE && y == -1)
                || (y == Integer.MIN_VALUE && x == -1)) {
            throw new ArithmeticException("Integer overflow");
        }
	}
	
	private void checkDivide(Integer x, Integer y) {
        if (y == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new ArithmeticException("Integer overflow");
        }
	}
	
	private void checkNegate(Integer x) {
		if (x == Integer.MIN_VALUE) {
			throw new ArithmeticException("Integer overflow");
		}
	}
}