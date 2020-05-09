import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation.
 */
public class Percolation {
    /**
     * Number of virtual sites.
     */
    private static final int VIRTUAL_SITES = 2;

    /**
     * Size of the array.
     */
    private final int size;

    /**
     * Side of the square.
     */
    private final int side;

    /**
     * Number of open sites.
     */
    private int numberOfOpenSites = 0;

    /**
     * Open sites.
     */
    private final boolean[] openSites;

    /**
     * Quick Union class.
     */
    private final WeightedQuickUnionUF storage;

    /**
     * Quick Union class.
     */
    private final WeightedQuickUnionUF backup;

    /**
     * Top virtual site.
     */
    private final int topVirtualSite;

    /**
     * Bottom virtual site.
     */
    private final int bottomVirtualSite;

    /**
     * creates n-by-n grid, with all sites initially blocked.
     * @param n - number of elements.
     */
    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be greater than zero");
        }

        side = n;
        size = n * n;

        // Plus three virtual sites
        storage = new WeightedQuickUnionUF(size + VIRTUAL_SITES);
        backup = new WeightedQuickUnionUF(size + VIRTUAL_SITES);

        openSites = new boolean[size];
        for (int i = 0; i < size; i++) {
            openSites[i] = false;
        }

        topVirtualSite = storage.find(size);
        bottomVirtualSite = storage.find(size + 1);
    }

    /**
     * opens the site (row, col) if it is not open already.
     * @param row - number of rows.
     * @param col - number of columns.
     */
    public void open(final int row, final int col) {
        final int index = indexOf(row, col);

        // The site is already open
        if (openSites[index]) {
            return;
        }

        openSites[index] = true;
        numberOfOpenSites += 1;

        if (row == 1) { // Connect with top virtual site
            storage.union(topVirtualSite, index);
            backup.union(topVirtualSite, index);
        } else if (row > 1 && isOpen(row - 1, col)) {
            storage.union(index, indexOf(row - 1, col));
            backup.union(index, indexOf(row - 1, col));
        }

        if (row == side) { // Connect with bottom virtual site
            storage.union(bottomVirtualSite, index);
        } else if (row < side && isOpen(row + 1, col)) {
            storage.union(index, indexOf(row + 1, col));
            backup.union(index, indexOf(row + 1, col));
        }

        if (col > 1 && isOpen(row, col - 1)) {
            storage.union(index, indexOf(row, col - 1));
            backup.union(index, indexOf(row, col - 1));
        }

        if (col < side && isOpen(row, col + 1)) {
            storage.union(index, indexOf(row, col + 1));
            backup.union(index, indexOf(row, col + 1));
        }
    }

    /**
     * is the site (row, col) open?
     * @param row - number of rows.
     * @param col - number of columns.
     * @return - is the site (row, col) open?
     */
    public boolean isOpen(final int row, final int col) {
        return openSites[indexOf(row, col)];
    }

    /**
     * is the site (row, col) full?
     * @param row - number of rows.
     * @param col - number of columns.
     * @return - is the site (row, col) full?
     */
    public boolean isFull(final int row, final int col) {
        int index = indexOf(row, col);
        if (!isOpen(row, col)) {
            return false;
        }

        int indexParent = backup.find(index);
        int topParent = backup.find(topVirtualSite);

        return indexParent == topParent;
    }

    /**
     * returns the number of open sites.
     * @return - number of open sites.
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * does the system percolate?
     * @return - does the system percolate?
     */
    public boolean percolates() {
        return storage.find(topVirtualSite) == storage.find(bottomVirtualSite);
    }

    // MARK: - Main

    /**
     * test client (see below).
     * @param args - command line arguments.
     */
    public static void main(final String[] args) {
        final Percolation percolation = new Percolation(2);

        while (!percolation.percolates()) {
            final int row = StdRandom.uniform(percolation.side) + 1;
            final int col = StdRandom.uniform(percolation.side) + 1;

            percolation.open(row, col);
        }
    }

    // MARK: - Private

    private int indexOf(final int row, final int col) {
        if (row < 1 || row > side) {
            throw new IllegalArgumentException(
                    "index is not in range: 1..." + side
            );
        }

        if (col < 1 || col > side) {
            throw new IllegalArgumentException(
                    "index is not in range: 1..." + side
            );
        }

        final int index = (row - 1) * side + col - 1;
        if (index >= size) {
            throw new IllegalArgumentException(
                    "index is greater than size: " + (size - 1)
            );
        }

        return index;
    }
}
