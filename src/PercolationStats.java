import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * PercolationStats.
 */
public class PercolationStats {
    /**
     * Constant.
     */
    private static final double MAGIC = 1.96;

    /**
     * Number of trials.
     */
    private final int numberOfTrials;

    /**
     * Mean.
     */
    private final double mean;

    /**
     * Stddev.
     */
    private final double stddev;

    /**
     * perform independent trials on an n-by-n grid.
     * @param n - size of the square.
     * @param trials - number of trials.
     */
    public PercolationStats(final int n, final int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be greater than zero");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("n should be greater than zero");
        }

        numberOfTrials = trials;
        double[] thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                percolation.open(row, col);
            }

            final int numberOfOpenSites = percolation.numberOfOpenSites();
            final double threshold = (double) numberOfOpenSites / (n * n);
            thresholds[i] = threshold;
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
    }

    /**
     * sample mean of percolation threshold.
     * @return - sample mean of percolation threshold.
     */
    public double mean() {
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold.
     * @return - sample standard deviation of percolation threshold.
     */
    public double stddev() {
        return stddev;
    }

    /**
     * low endpoint of 95% confidence interval.
     * @return - low endpoint of 95% confidence interval.
     */
    public double confidenceLo() {
        return mean() - MAGIC * stddev() / Math.sqrt(numberOfTrials);
    }

    /**
     * high endpoint of 95% confidence interval.
     * @return - high endpoint of 95% confidence interval.
     */
    public double confidenceHi() {
        return mean() + MAGIC * stddev() / Math.sqrt(numberOfTrials);
    }

    /**
     * test client (see below).
     * @param args - command line arguments.
     */
    public static void main(final String[] args) {
        if (args.length < 2) {
            System.out.println("Add two arguments");
            return;
        }

        int n = 0;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Argument '" + args[0]
                    + "' must be an integer.");
            return;
        }

        int t = 0;
        try {
            t = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Argument '" + args[1]
                    + "' must be an integer.");
            return;
        }

        PercolationStats stats = new PercolationStats(n, t);

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = ["
                + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
