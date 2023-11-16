public class ExponentialLinearProgrammingSolver {
    public static void main(String[] args) {
        // Example usage
        int n = 3; // You can change the size of n
        int[][] A = {{3, 8, 5}, {2, 3, 1}, {9, 6, 0}};
        int[] b = {5, 12, 2};
        int[] c = {8, 9, 1};
        int[] s = {3, 3, 1};

        int[] x = solveExponentialLinearProgramming(n, A, b, c, s);

        // Print the result
        if (x != null) {
            System.out.println("Optimal values for x:");
            for (int i = 0; i < n; i++) {
                System.out.println("x" + i + ": " + x[i]);
            }
        } else {
            System.out.println("No valid solution found.");
        }
    }

    public static int[] solveExponentialLinearProgramming(int n, int[][] A, int[] b, int[] c, int[] s) {
        int[] bestX = new int[n];
        int maxDotProduct = Integer.MIN_VALUE;

        // Iterate through all possible combinations of x values
        for (int i = 0; i <= s[0]; i++) {
            for (int j = 0; j <= s[1]; j++) {
                for (int k = 0; k <= s[2]; k++) {
                    int[] currentX = {i, j, k};

                    if (isValidSolution(n, A, b, currentX, s)) {
                        int dotProduct = calculateDotProduct(c, currentX);
                        if (dotProduct > maxDotProduct) {
                            maxDotProduct = dotProduct;
                            System.arraycopy(currentX, 0, bestX, 0, n);
                        }
                    }
                }
            }
        }

        // Check if any valid solution was found
        if (isValidSolution(n, A, b, bestX, s)) {
            return bestX;
        } else {
            return null;  // No valid solution found
        }
    }

    private static boolean isValidSolution(int n, int[][] A, int[] b, int[] x, int[] s) {
        for (int i = 0; i < n; i++) {
            int lhs = 0;
            for (int j = 0; j < n; j++) {
                lhs += A[i][j] * x[j];
            }
            if (lhs + s[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private static int calculateDotProduct(int[] c, int[] x) {
        int result = 0;
        for (int i = 0; i < c.length; i++) {
            result += c[i] * x[i];
        }
        return result;
    }
}
