package ticTacToe;

public class BoardInterface implements Position {
    MnkBoard board;
    public BoardInterface(MnkBoard board) {
        this.board = board;
    }

    public boolean isValid(Move move) {
        return board.isValid(move);
    }

    public Cell getCell(int r, int c) {
        return board.getCell(r, c);
    }

    public String toString() {
        return board.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof MnkBoard)) {
            return false;
        }
        System.err.println("bruh?");
        for (int i = 0; i < board.m; i++) {
            for (int j = 0; j < board.n; j++) {
                if (board.getCell(i, j) != ((MnkBoard) o).getCell(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}

