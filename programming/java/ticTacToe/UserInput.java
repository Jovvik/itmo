package ticTacToe;

import java.util.Scanner;

public class UserInput {
    static int[] get(int n, Scanner sc) {
        int[] vals = new int[n];
        int valsRead = 0;
        while (valsRead != n) {
            if (sc.hasNextInt()) {
                vals[valsRead++] = sc.nextInt();
            } else {
                System.out.println("Wrong input, integer expected");
                sc.next();
            }
        }
        return vals;
    }
}