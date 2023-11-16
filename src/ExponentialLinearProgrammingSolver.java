import java.util.Arrays;

public class ExponentialLinearProgrammingSolver {
    public static int[] solveLinearProgram(int numberOfVariables, int[] objectiveFunctionCoefficients, int[] constraintConstants, int[][] constraintMatrix, int[] slackVariables) {
        int[][] augmentedMatrix = constructAugmentedMatrix(constraintMatrix, slackVariables, constraintConstants);
        int[] solution = solveSystemOfEquations(augmentedMatrix);

        return Arrays.copyOfRange(solution, 0, numberOfVariables);
    }

    private static int[][] constructAugmentedMatrix(int[][] constraintMatrix, int[] slackVariables, int[] constraintConstants) {
        int numberOfRows = constraintMatrix.length;
        int numberOfColumns = constraintMatrix[0].length + 1;
        int[][] augmentedMatrix = new int[numberOfRows][numberOfColumns];

        // Construct augmented matrix
        for (int i = 0; i < numberOfRows; i++) {
            System.arraycopy(constraintMatrix[i], 0, augmentedMatrix[i], 0, constraintMatrix[i].length);
            augmentedMatrix[i][numberOfColumns - 1] = constraintConstants[i] - slackVariables[i];
        }

        return augmentedMatrix;
    }

    private static int[] solveSystemOfEquations(int[][] augmentedMatrix) {
        int numberOfRows = augmentedMatrix.length;
        int numberOfVariables = augmentedMatrix[0].length - 1;

        // Gaussian elimination
        for (int i = 0; i < numberOfRows; i++) {
            int pivotRow = i;

            // Find pivot element
            while (pivotRow < numberOfRows && augmentedMatrix[pivotRow][i] == 0) {
                pivotRow++;
            }

            // If no pivot element found, continue to the next column
            if (pivotRow == numberOfRows) {
                continue;
            }

            // Swap rows to move pivot element to the current row
            if (pivotRow != i) {
                int[] temp = augmentedMatrix[i];
                augmentedMatrix[i] = augmentedMatrix[pivotRow];
                augmentedMatrix[pivotRow] = temp;
            }

            int pivotValue = augmentedMatrix[i][i];

            // Normalize the current row
            for (int j = i; j <= numberOfVariables; j++) {
                augmentedMatrix[i][j] /= pivotValue;
            }

            // Eliminate other rows
            for (int k = 0; k < numberOfRows; k++) {
                if (k != i && augmentedMatrix[k][i] != 0) {
                    int factor = augmentedMatrix[k][i];
                    for (int j = i; j <= numberOfVariables; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        // Extract solution from the augmented matrix
        int[] solution = new int[numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            solution[i] = augmentedMatrix[i][numberOfVariables];
        }

        return solution;
    }

    public static void main(String[] args) {
        // Example usage
        int numberOfVariables = 2;
        int[] objectiveFunctionCoefficients = {2, 3};
        int[] constraintConstants = {4, 6};
        int[][] constraintMatrix = {{1, 2}, {2, 1}};
        int[] slackVariables = {0, 0};

        int[] solution = solveLinearProgram(numberOfVariables, objectiveFunctionCoefficients, constraintConstants, constraintMatrix, slackVariables);

// Print solution without ensuring non-negativity
        System.out.println("Optimal values for x:");
        for (int i = 0; i < numberOfVariables; i++) {
            System.out.println("x[" + i + "] = " + solution[i]);
        }
    }
}
