package ticTacToe;

import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = 0;
        int n = 0;
        int k = 0;
        int c = 0;
        do {
            int[] params = UserInput.get(4, sc);
            m = params[0];
            n = params[1];
            k = params[2];
            c = params[3];
        } while(m <= 0 || n <= 0 || k > m || k > n);
        // final Tournament tournament = new Tournament(m, n, k, c);
        // final Game game = new Game(true, new RandomPlayer(m,n), new RandomPlayer(m,n), new RandomPl>
        // final Game game = new Game(true, new HumanPlayer(), new Winner(m, n, k, Cell.X));
        // int result = game.play(new MnkBoard(m, n, k, true));
        // System.out.println("Game result: " + result);
        final Match match = new Match(false, m, n, k, c, new RandomPlayer(), new Winner(m, n, k, Cell.X));
        match.play();
    }
}