import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private int trials;
    private double[] fractions;

    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("N or trials are <= 0");
        this.trials = trials;
        this.fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(row, col))
                    percolation.open(row, col);
            }
            fractions[i] = (percolation.numberOfOpenSites() * 1.0) / (n * n);
        }
    }

    public double mean() {                       // sample mean of percolation threshold
        double value = 0;
        for (int i = 0; i < fractions.length; i++)
            value += fractions[i];
        return value / trials;
    }

    public double stddev() {                        // sample standard deviation of percolation threshold
        double mu = this.mean();
        double dev = 0;
        for (int i = 0; i < fractions.length; i++)
            dev += (fractions[i] - mu) * (fractions[i] - mu);
        return Math.sqrt(dev / (trials - 1));
    }

    public double confidenceLo() {                // low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi() { // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {        // test client (described below)
        PercolationStats ps = new PercolationStats(200, 100);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());
        System.out.println(ps.confidenceHi());

    }
}