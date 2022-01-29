package expression.type;

public interface Type<T> {

    T add(T a, T b);
    
    T subtract(T a, T b);
    
    T multiply(T a, T b);
    
    T divide(T a, T b);
    
    T negate(T a);
    
    T parseNumber(String number);

    T castInt(int number);
}
