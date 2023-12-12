public class Test {
    public static void main(String[] args) {
        BruteForceSudoku sudokuSolver = new BruteForceSudoku();

        long timeA = System.nanoTime();
        // Test Case 1: Easy Puzzle
        int[][] easyPuzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        System.out.println("Easy Puzzle:");
        printSudokuGrid(easyPuzzle);
        boolean solved1 = sudokuSolver.solvePuzzle(easyPuzzle);
        System.out.println("Solved Puzzle:");
        printSudokuGrid(easyPuzzle);
        System.out.println("Is Solved: " + solved1);
        System.out.println();
        long timeB = System.nanoTime();
        System.out.println("Elapsed time: " + (timeB - timeA));

        // Test Case 2: Medium Puzzle
        long timeC = System.nanoTime();
        int[][] mediumPuzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 0, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        System.out.println("Medium Puzzle:");
        printSudokuGrid(mediumPuzzle);
        boolean solved2 = sudokuSolver.solvePuzzle(mediumPuzzle);
        System.out.println("Solved Puzzle:");
        printSudokuGrid(mediumPuzzle);
        System.out.println("Is Solved: " + solved2);
        System.out.println();
        long timeD = System.nanoTime();
        System.out.println("Elapsed time: " + (timeD - timeC));

        // Test Case 3: Hard Puzzle
        long timeE = System.nanoTime();
        int[][] hardPuzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 7, 0, 1, 9, 5, 0, 0, 0},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 0, 7, 6, 1, 0, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 0, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        System.out.println("Hard Puzzle:");
        printSudokuGrid(hardPuzzle);
        boolean solved3 = sudokuSolver.solvePuzzle(hardPuzzle);
        System.out.println("Solved Puzzle:");
        printSudokuGrid(hardPuzzle);
        System.out.println("Is Solved: " + solved3);
        long timeF = System.nanoTime();
        System.out.println(timeF);
        System.out.println("Elapsed time: " + (timeF - timeE));
    }

    private static void printSudokuGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
