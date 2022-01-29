package ticTacToe;

import java.util.*;

public class Match {
    int m, n, k, c;
    boolean log;
    List<Player> players;
    public Match(boolean log, int m, int n, int k, int c, Player firstPlayer, Player secondPlayer) {
        this.log = log;
        this.m = m;
        this.n = n;
        this.k = k;
        this.c = c;
        this.players = new ArrayList<>(List.of(firstPlayer, secondPlayer));
    }

    public void play() {
        int[] wins = new int[players.size()];
        int shift = 0;
        while (wins[0 + shift] != c && wins[1 - shift] != c) {
            Game game = new Game(log, players.get(0 + shift), players.get(1 - shift));
            final int result = game.play(new MnkBoard(m, n, k, true));
            if (result == 0) {
                System.out.println("Draw");
            } else if (result == 1 || result == 3) {
                System.out.println("Player " + (1 + shift) + " won");
                wins[0 + shift]++;
            } else {
                System.out.println("Player " + (2 - shift) + " won");
                wins[1 - shift]++;
            }
            shift = (shift + 1) % 2;
        }
        System.out.println(wins[0] + ":" + wins[1]);
    }
}