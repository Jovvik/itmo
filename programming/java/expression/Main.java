package expression;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map.of(1, 2);
        // System.out.println(new Add(new Variable("a"), new Subtract(new Variable("b"), new Variable("c"))).toMiniString());
        // if (args.length != 1) {
        //     throw new IllegalArgumentException("1 argument required");
        // }
        // int x = Integer.parseInt(args[0]);
        
        // System.out.println(new Add(new Variable("x"), new Const(2)).equals(new Add(new Variable("x"), new Const(2))));

        // System.out.println(
        //     new Add(
        //         new Subtract(
        //             new Multiply(
        //                 new Variable("x"),
        //                 new Variable("y")),
        //             new Multiply(
        //                 new Variable("z"),
        //                 new Const(2))
        //         ),
        //         new Const(1)
        //     ).evaluate(5, 4, 3));

        // System.out.println(new Add(new Variable("x"), new Const(2)).evaluate(3));
    }
}