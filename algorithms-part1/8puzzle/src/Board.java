import java.util.Arrays;

import edu.princeton.cs.algs4.Stack;

public class Board {

    private int[][] blocks;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new NullPointerException("Null argument");
        this.blocks = copy(blocks);
    }


    public int dimension() { // board dimension n
        return blocks.length;
    }

    public int hamming() { // number of blocks out of place
        int distance = 0;
        int shouldBe = 1;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (blocks[i][j] != shouldBe++) distance++;
        // remove 1 because blocks[n-1][n-1] should be 0, not 9
        return distance - 1;
    }

    public int manhattan() { // sum of Manhattan distances between blocks and goal
        int distance = 0;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0) {
                    // -1 because value [0][0] is position for 1, not 0
                    int row = (blocks[i][j] - 1) / dimension();
                    int col = (blocks[i][j] - 1) % dimension();
                    distance += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        return distance;
    }

    public boolean isGoal() { // is this board the goal board?
        return this.hamming() == 0;
    }

    public Board twin() { // a board that is obtained by exchanging any pair of blocks
        int[][] newBlocks = copy(blocks);
        if (newBlocks[0][0] == 0) {
            exchange(newBlocks, 0, 1, 1, 0);
        } else {
            if (newBlocks[0][1] == 0)
                exchange(newBlocks, 0, 0, 1, 0);
            else
                exchange(newBlocks, 0, 0, 0, 1);
        }
        return new Board(newBlocks);
    }


    private void exchange(int[][] array, int i, int j, int i2, int j2) {
        int temp = array[i][j];
        array[i][j] = array[i2][j2];
        array[i2][j2] = temp;
    }

    public boolean equals(Object y) { // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        if (other.dimension() != this.dimension()) return false;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (blocks[i][j] != other.blocks[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() { // all neighboring boards
        Stack<Board> neighbours = new Stack<>();

        // finding position of blank
        int blankRow = -1;
        int blankColumn = -1;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankColumn = j;
                    break;
                }
        if (blankRow > 0) {
            int[][] newBlocks = copy(blocks);
            exchange(newBlocks, blankRow, blankColumn, blankRow - 1, blankColumn);
            neighbours.push(new Board(newBlocks));
        }
        if (blankRow < dimension() - 1) {
            int[][] newBlocks = copy(blocks);
            exchange(newBlocks, blankRow, blankColumn, blankRow + 1, blankColumn);
            neighbours.push(new Board(newBlocks));
        }
        if (blankColumn > 0) {
            int[][] newBlocks = copy(blocks);
            exchange(newBlocks, blankRow, blankColumn, blankRow, blankColumn - 1);
            neighbours.push(new Board(newBlocks));
        }
        if (blankColumn < dimension() - 1) {
            int[][] newBlocks = copy(blocks);
            exchange(newBlocks, blankRow, blankColumn, blankRow, blankColumn + 1);
            neighbours.push(new Board(newBlocks));
        }
        return neighbours;
    }

    public String toString() { // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int[][] copy(int[][] input) {
        int[][] target = new int[input.length][];
        for (int i = 0; i < input.length; i++) {
            target[i] = Arrays.copyOf(input[i], input[i].length);
        }
        return target;
    }

    public static void main(String[] args) { // unit tests (not graded)
        int[][] blocks = new int[3][];
        blocks[0] = new int[]{1, 2, 3};
        blocks[1] = new int[]{4, 5, 6};
        blocks[2] = new int[]{7, 8, 0};


        Iterable<Board> list = new Board(blocks).neighbors();
//        System.out.println();
        list.forEach(System.out::print);
        //  list.forEach(x -> x.neighbors().forEach(System.out::print));
    }
}
