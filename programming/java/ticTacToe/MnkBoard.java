package ticTacToe;

import java.util.Arrays;
import java.util.Map;

public class MnkBoard implements Board, Position {
    private static final Map<Cell, String> SYMBOLS = Map.of(
        Cell.X, "\033[0;35mX\033[0m",
        Cell.O, "\033[0;36mO\033[0m",
        Cell.E, "\u00B7"
    );

    private final Cell[][] cells;
    private Cell turn;
    public int m, n, k;
    private int totalEmpty;
    private boolean doCompress;

    public MnkBoard(int m, int n, int k, boolean doCompress) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.totalEmpty = m * n;
        this.cells = new Cell[m][n];
        this.doCompress = doCompress;
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    public MnkBoard() {
        this(3,3,3, false);
    }

    public MnkBoard(MnkBoard otherBoard, int m, int n, int k) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.doCompress = false;
        turn = otherBoard.getCell();
        cells = new Cell[m][n];
        totalEmpty = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cells[i][j] = otherBoard.getCell(i, j);
                totalEmpty += cells[i][j] == Cell.E ? 1 : 0;
            }
        }
    }

    public int getTotalEmpty() {
        return totalEmpty;
    }

    @Override
    public Position getPosition() {
        return new BoardInterface(this);
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();
        totalEmpty--;

        int colMax = 0;
        int rowMax = 0;
        int diag1Max = 0;
        int diag2Max = 0;
        
        for (int xDir = -1; xDir <= 1; xDir++) {
            for (int yDir = -1; yDir <= 1; yDir++) {
                if (xDir == 0 && yDir == 0) {
                    continue;
                }
                if (check(move.getRow(), move.getColumn(), xDir, yDir, move.getValue())) {
                    return Result.WIN;
                }
            }
        }

        totalEmpty--;
        if (totalEmpty == 0) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        // turn = turn == Cell.X ? Cell.O : (turn == Cell.O ? Cell.A : (turn == Cell.A ? Cell.B : Cell.X));
        return Result.UNKNOWN;
    }

    private boolean check(int startx, int starty, int xDir, int yDir, Cell val) {
        int len = 0;
        for (int diff = -k; diff <= k; diff++) {
            if (checkPos(startx + xDir * diff, starty + yDir * diff, val)) {
                len++;
                if (len == k) {
                    return true;
                }
            } else {
                len = 0;
            }
        }
        return false;
    }

    private boolean checkPos(int x, int y, Cell val) {
        if (x < 0 || y < 0 || x >= m || y >= n) {
            return false;
        }
        return val == cells[x][y];
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getColumn() && move.getColumn() < n
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == move.getValue();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" ");
        String columnFormatString = buildFormattingString(n);
        String rowFormatString = buildFormattingString(m);
        sb.append(" ".repeat(String.valueOf(m).length()));
        for (int c = 0; c < n; c++) {
            if (doCompress) {
                sb.append(c % 10 + " ");
            } else {
                sb.append(String.format(columnFormatString, String.valueOf(c)));
            }
        }
        for (int r = 0; r < m; r++) {
            sb.append("\n");
            sb.append(String.format(rowFormatString, String.valueOf(r)));
            for (int c = 0; c < n; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
                if (doCompress) {
                    sb.append(" ");
                } else
                sb.append(" ".repeat(String.valueOf(n).length()));
            }
        }
        return sb.toString();
    }

    private String buildFormattingString(int val) {
        return "%-" + (String.valueOf(val).length() + 1) + "s";
    }
}