package expression.type;

public class IntegerType implements Type<Integer> {

    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        return a / b;
    }

    @Override
    public Integer negate(Integer a) {
        return -a;
    }

    @Override
    public Integer parseNumber(String number) {
        return Integer.parseInt(number);
    }

    @Override
    public Integer castInt(int number) {
        return Integer.valueOf(number);
    }
    
}