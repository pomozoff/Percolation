import edu.princeton.cs.algs4.StdIn;

final class Reader {
    public static void main(final String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);

        while (!StdIn.isEmpty() && !percolation.percolates()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();

            percolation.open(row, col);

            assert percolation.isOpen(row, col) : "[" + row + ", " + col + "] - should be open";
            assert percolation.isFull(3, 3) : "[3, 3] - Should not be full";
        }
    }

    private Reader() {
    }
}
