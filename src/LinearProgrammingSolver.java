public class LinearProgrammingSolver {
    public static void main(String[] args) {
        // Example usage
        int n = 3; // You can change the size of n
        double[][] A = {{3.2, 8.7, 5.9}, {2.4, 3.1, 1.1}, {9.7, 6.1, 0.3}};
        double[] b = {5, 12.9, 2};
        double[] c = {8.2, 9.7, 1.1};
        double[] s = {3.7, 3.8, 1};

        double[] x = solveLinearProgramming(n, A, b, c, s);

        // Print the result
        System.out.println("Optimal values for x:");
        for (int i = 0; i < n; i++) {
            System.out.println("x[" + i + "] = " + x[i]);
        }
    }

    public static double[] solveLinearProgramming(int n, double[][] A, double[] b, double[] c, double[] s) {
        // Add slack variables to convert inequalities to equalities
        double[][] tableau = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tableau[i][j] = A[i][j];
            }
            tableau[i][n] = s[i];
        }

        // Add the objective function to the tableau
        double[] objectiveRow = new double[n + 1];
        for (int i = 0; i < n; i++) {
            objectiveRow[i] = -c[i];
        }
        tableau[n - 1] = objectiveRow;

        // Perform the Simplex method
        while (true) {
            int pivotCol = findPivotColumn(tableau);
            if (pivotCol == -1) {
                break; // Solution found
            }

            int pivotRow = findPivotRow(tableau, pivotCol);
            if (pivotRow == -1) {
                throw new ArithmeticException("The problem is unbounded");
            }

            pivotOn(tableau, pivotRow, pivotCol);
        }

        // Extract the solution from the tableau
        double[] solution = new double[n];
        for (int i = 0; i < n; i++) {
            solution[i] = tableau[i][n];
        }

        return solution;
    }

    private static int findPivotColumn(double[][] tableau) {
        int m = tableau.length;
        int n = tableau[0].length - 1;

        // Find the most negative entry in the bottom row
        int pivotCol = -1;
        for (int j = 0; j < n; j++) {
            if (tableau[m - 1][j] < 0 && (pivotCol == -1 || tableau[m - 1][j] < tableau[m - 1][pivotCol])) {
                pivotCol = j;
            }
        }

        return pivotCol;
    }

    private static int findPivotRow(double[][] tableau, int pivotCol) {
        int m = tableau.length;
        int n = tableau[0].length - 1;

        // Find the pivot row with the minimum ratio
        int pivotRow = -1;
        for (int i = 0; i < m - 1; i++) {
            if (tableau[i][pivotCol] > 0) {
                if (pivotRow == -1 || (tableau[i][n] / tableau[i][pivotCol]) < (tableau[pivotRow][n] / tableau[pivotRow][pivotCol])) {
                    pivotRow = i;
                }
            }
        }

        return pivotRow;
    }

    private static void pivotOn(double[][] tableau, int pivotRow, int pivotCol) {
        int m = tableau.length;
        int n = tableau[0].length;

        // Make the pivot element 1
        double pivot = tableau[pivotRow][pivotCol];
        for (int j = 0; j < n; j++) {
            tableau[pivotRow][j] /= pivot;
        }

        // Make the other elements in the pivot column 0
        for (int i = 0; i < m; i++) {
            if (i != pivotRow) {
                double factor = tableau[i][pivotCol];
                for (int j = 0; j < n; j++) {
                    tableau[i][j] -= factor * tableau[pivotRow][j];
                }
            }
        }
    }
}