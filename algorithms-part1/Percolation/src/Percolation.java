import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF backwash;
    private boolean[] open;
    private int virtualTop;
    private int virtualBottom;
    private int openSites;
    private int n;

    public Percolation(int n) {                // create n-by-n grid, with all sites blocked
        if (n <= 0) throw new IllegalArgumentException("N is <= 0");
        grid = new WeightedQuickUnionUF(n * n + 2); // adding 2 for virtual top and virtual bottom
        backwash = new WeightedQuickUnionUF(n * n + 2);

        open = new boolean[grid.count()];
        virtualTop = 0;
        virtualBottom = grid.count() - 1;

        for (int i = 1; i <= n; i++) {
            grid.union(this.virtualTop, i);
            backwash.union(this.virtualTop, i);
            grid.union(this.virtualBottom, (n - 1) * n + i);
        }

        openSites = 0;
        this.n = n;
    }

    private int id(int row, int col) { // give row and column, get index in array
        return (row - 1) * n + col;
    }

    public void open(int row, int col) {    // open site (row, col) if it is not open already
        if (row < 1 || row > n)
            throw new IndexOutOfBoundsException("row out of bounds");
        if (col < 1 || col > n)
            throw new IndexOutOfBoundsException("column out of bounds");
        if (!isOpen(row, col)) {
            if (row > 1 && isOpen(row - 1, col)) {
                grid.union(id(row, col), id(row - 1, col));
                backwash.union(id(row, col), id(row - 1, col));
            }
            if (row < n && isOpen(row + 1, col)) {
                grid.union(id(row, col), id(row + 1, col));
                backwash.union(id(row, col), id(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) {
                grid.union(id(row, col), id(row, col - 1));
                backwash.union(id(row, col), id(row, col - 1));
            }
            if (col < n && isOpen(row, col + 1)) {
                grid.union(id(row, col), id(row, col + 1));
                backwash.union(id(row, col), id(row, col + 1));
            }

            open[id(row, col)] = true;
            openSites++;
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        if (row < 1 || row > n)
            throw new IndexOutOfBoundsException("row out of bounds");
        if (col < 1 || col > n)
            throw new IndexOutOfBoundsException("column out of bounds");
        return open[id(row, col)];
    }

    public boolean isFull(int row, int col) {  // is site (row, col) full?
        return isOpen(row, col) && backwash.connected(virtualTop, id(row, col));
    }

    public int numberOfOpenSites() {  // number of open sites
        return this.openSites;
    }

    public boolean percolates() {            // does the system percolate?
        return numberOfOpenSites() > 0 && grid.connected(virtualTop, virtualBottom);
    }

    public static void main(String[] args) {  // test client (optional)
        Percolation pc = new Percolation(1);
        pc.open(1, 1);
        System.out.println(pc.percolates());

    }
}