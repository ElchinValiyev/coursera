import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;


public class Solver {
    private SearchNode resultingNode;
    private boolean isSolvable = false;

    public Solver(Board initial) { // find a solution to the initial board (using the A* algorithm)
        aStar(initial, initial.twin());
    }

    private void aStar(Board initial, Board twin) {
        MinPQ<SearchNode> nodes = new MinPQ<>();
        MinPQ<SearchNode> twinNodes = new MinPQ<>();

        nodes.insert(new SearchNode(initial, null, 0));
        twinNodes.insert(new SearchNode(twin, null, 0));

        SearchNode current = null;
        SearchNode twinCurrent = null;

        do {
            current = nodes.delMin();
            current.neighbors().forEach(nodes::insert);

            twinCurrent = twinNodes.delMin();
            twinCurrent.neighbors().forEach(twinNodes::insert);

        } while (!(current.isGoal() || twinCurrent.isGoal()));

        if (current.isGoal()) {
            resultingNode = current;
            isSolvable = true;
        }
    }

    public boolean isSolvable() {           // is the initial board solvable?
        return isSolvable;
    }

    public int moves() { // min number of moves to solve initial board; -1 if unsolvable
        return isSolvable ? resultingNode.moves : -1;
    }

    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        return isSolvable ? resultingNode.solution() : null;
    }


    public static void main(String[] args) { // solve a slider puzzle (given below)
        int[][] blocks = new int[2][];
        blocks[0] = new int[]{0, 1};
        blocks[1] = new int[]{2, 3};
        Solver sl = new Solver(new Board(blocks));
        System.out.println(sl.isSolvable);
    }

    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode previous;
        private Board current;
        private int moves;
        private int priority;


        SearchNode(Board board, SearchNode previous, int moves) {
            this.current = board;
            this.previous = previous;
            this.moves = moves;
            this.priority = current.manhattan() + moves;
        }

        public Iterable<SearchNode> neighbors() {
            Stack<SearchNode> stack = new Stack<>();
            for (Board item : current.neighbors())
                if (previous == null || !item.equals(previous.current))
                    stack.push(new SearchNode(item, this, moves + 1));
            return stack;
        }

        private boolean isGoal() {
            return current.isGoal();
        }

        private Iterable<Board> solution() {
            Stack<Board> boards = new Stack<>();
            SearchNode node = this;
            while (node != null) {
                boards.push(node.current);
                node = node.previous;
            }
            return boards;
        }

        @Override
        public int compareTo(SearchNode other) {
            return this.priority - other.priority;
        }
    }

}

