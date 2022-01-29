package expression.parser;

public class Main {
    public static void main(String[] args) {
        Parser parser = new ExpressionParser();
        System.out.println(parser.parse("-2"));
    }
}