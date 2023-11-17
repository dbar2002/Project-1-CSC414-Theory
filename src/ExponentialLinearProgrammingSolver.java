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

        for (int i = 0; i < numberOfRows; i++) {
            System.arraycopy(constraintMatrix[i], 0, augmentedMatrix[i], 0, constraintMatrix[i].length);
            augmentedMatrix[i][numberOfColumns - 1] = constraintConstants[i] - slackVariables[i];
        }

        return augmentedMatrix;
    }

    private static int[] solveSystemOfEquations(int[][] augmentedMatrix) {
        int numberOfRows = augmentedMatrix.length;
        int numberOfVariables = augmentedMatrix[0].length - 1;

        for (int i = 0; i < numberOfRows; i++) {
            int pivotRow = i;

            while (pivotRow < numberOfRows && augmentedMatrix[pivotRow][i] == 0) {
                pivotRow++;
            }

            if (pivotRow == numberOfRows) {
                continue;
            }

            if (pivotRow != i) {
                int[] temp = augmentedMatrix[i];
                augmentedMatrix[i] = augmentedMatrix[pivotRow];
                augmentedMatrix[pivotRow] = temp;
            }

            int pivotValue = augmentedMatrix[i][i];

            for (int j = i; j <= numberOfVariables; j++) {
                augmentedMatrix[i][j] /= pivotValue;
            }

            for (int k = 0; k < numberOfRows; k++) {
                if (k != i && augmentedMatrix[k][i] != 0) {
                    int factor = augmentedMatrix[k][i];
                    for (int j = i; j <= numberOfVariables; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        int[] solution = new int[numberOfVariables];
        for (int i = 0; i < numberOfRows; i++) {
            solution[i] = augmentedMatrix[i][numberOfVariables];
        }

        return solution;
    }

    public static void printSolution(int[] solution, int[] objectiveFunctionCoefficients) {
        System.out.println("Optimal solution:");
        for (int i = 0; i < solution.length; i++) {
            System.out.println("x[" + i + "] = " + solution[i]);
        }

        int objectiveValue = calculateObjectiveValue(solution, objectiveFunctionCoefficients);
        System.out.println("Optimal objective function value: " + objectiveValue);
    }

    private static int calculateObjectiveValue(int[] solution, int[] objectiveFunctionCoefficients) {
        int value = 0;
        for (int i = 0; i < solution.length; i++) {
            value += solution[i] * objectiveFunctionCoefficients[i];
        }
        return value;
    }

    public static void main(String[] args) {
        int numberOfVariables = 3;
        int[] objectiveFunctionCoefficients = {2, 3, 4};
        int[] constraintConstants = {4, 6, 8};
        int[][] constraintMatrix = {{1, 2, 3}, {2, 1, 3}};
        int[] slackVariables = {0, 0, 0};

        int[] solution = solveLinearProgram(numberOfVariables, objectiveFunctionCoefficients, constraintConstants, constraintMatrix, slackVariables);

        printSolution(solution, objectiveFunctionCoefficients);
    }
}
