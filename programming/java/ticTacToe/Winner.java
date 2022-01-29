package ticTacToe;

import java.util.*;

public class Winner implements Player {
    public TreeNode startNode;
    public Winner(int m, int n, int k, Cell startCell) {
        startNode = new TreeNode(new MnkBoard(m, n, k, false), m, n, k, Cell.X);
    }

    public Move move(final Position position, final Cell cell) {
        int idx = -1;
        // System.err.println(position);
        for (int i = 0; i < startNode.children.size(); i++) {
            // System.err.println(startNode.children.get(i).pos);
            if (position.equals(startNode.children.get(i).pos)) {
                idx = i;
                startNode = startNode.children.get(i);
            }
        }
        if (idx == -1) {
            throw new IllegalArgumentException("pos not found");
        }
        for (TreeNode node : startNode.children) {
            System.err.println(node.pos);
            System.err.println(node.getValue());
        }
        System.err.println("-".repeat(50));
        int moveIdx = startNode.children.indexOf(Collections.max(startNode.children, Comparator.comparing(TreeNode::getValue)));
        if (idx == -1) {
            throw new IllegalArgumentException("dafuq?");
        }
        Move retval = startNode.moves.get(moveIdx);
        startNode = startNode.children.get(moveIdx);
        return retval;
    }
}

final class TreeNode {
    public MnkBoard pos;
    public List<Move> moves = new ArrayList<>();
    public List<TreeNode> children = new ArrayList<>();
    public int winCount;
    public int loseCount;
    public int drawCount;

    public TreeNode(MnkBoard board, int m, int n, int k, Cell actor) {
        this.pos = board;
        if (board.getTotalEmpty() == 0) {
            this.winCount = 0;
            this.loseCount = 0;
            this.drawCount = 0;
            return;
        }
        // System.out.println(board);
        
        moves = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Move tempMove = new Move(i, j, actor);
                if (pos.isValid(tempMove)) {
                    moves.add(tempMove);
                    MnkBoard tempBoard = new MnkBoard(pos, m, n, k);
                    Result res = tempBoard.makeMove(tempMove);
                    children.add(new TreeNode(tempBoard, m, n, k, actor == Cell.X ? Cell.O : Cell.X));
                    switch (res) {
                        case UNKNOWN:
                            break;
                        case WIN:
                            winCount++;
                            break;
                        case LOSE:
                            loseCount++;
                            break;
                        case DRAW:
                            drawCount++;
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        for (TreeNode child : children) {
            this.winCount += child.loseCount;
            this.loseCount += child.winCount;
            this.drawCount += child.drawCount;
        }
    }

    public double getValue() {
        return (double)winCount / (winCount + loseCount + drawCount);
    }
}