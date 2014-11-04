package sudoku;

/**
 * This class contains the routines to solve a Sudoku puzzle using the Recursive
 * Backtracking method.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
public class RecursiveSolver {
    private SudokuBoard sb;

    /**
     * Initializes a new Recursive Solver.
     * 
     * @param sb the Sudoku to solve
     */
    public RecursiveSolver(SudokuBoard sb) {
        this.sb = sb;
    }

    /**
     * Returns the Sudoku board.
     * 
     * @return the Sudoku board
     */
    public SudokuBoard getBoard() {
        return sb;
    }

    /**
     * Enumerates all unassigned cells of a Sudoku board.
     * 
     * @return the list of unassigned cells in a Sudoku board as a 2D array
     */
    private int[][] listEmptyCells() {
        int nbEmpty = 0;
        int[][] emptyCells;
        int k = 0;

        for (int i = 0; i < sb.size; i++) {
            for (int j = 0; j < sb.size; j++) {
                if (sb.get(i, j) == 0)
                    nbEmpty++;
            }
        }
        emptyCells = new int[nbEmpty][2];

        for (int i = 0; i < sb.size; i++) {
            for (int j = 0; j < sb.size; j++) {
                if (sb.get(i, j) == 0) {
                    emptyCells[k][0] = i;
                    emptyCells[k][1] = j;
                    k++;
                }
            }
        }
        return emptyCells;
    }

    /**
     * Check if a number can go in a specified cell of the board and not break
     * Sudoku's rules.
     * 
     * @param cell the cell to be tested
     * @param trial the number to be tested
     * @return <code>true</code> if the number doesn't break any rule;
     *         <code>false</code> otherwise
     */
    private boolean isTrialValid(int[] cell, int trial) {
        int sqrtSize = (int) Math.sqrt(sb.size);
        int boxRow = (cell[0] / sqrtSize) * sqrtSize;
        int boxCol = (cell[1] / sqrtSize) * sqrtSize;

        // Check row, column and box rules
        for (int i = 0; i < sb.size; i++) {
            if (sb.get(cell[0], i) == trial
                    || sb.get(i, cell[1]) == trial
                    || sb.get(boxRow + (i % sqrtSize), boxCol + (i / sqrtSize)) == trial)
                return false;
        }
        return true;
    }

    /**
     * Creates the list of unassigned cells and starts the recursive procedure
     * with the first one (calls the recursive method {@link #solveBacktrack}).
     * 
     * @return <code>true</code> if the sudoku has a solution;
     *         <code>false</code> otherwise
     */
    public boolean solve() {
        int[][] emptyCells = listEmptyCells();
        return solveBacktrack(emptyCells, 0);
    }

    /**
     * Recursive Backtracking function carrying out brute-force trial and error,
     * trying to find a compatible value for each unassigned cell.
     * 
     * @param emptyCells the list of unassigned cells
     * @param ind the index indicating where the algorithm is at in the list of
     *            unassigned cells
     * @return <code>true</code> if the sudoku has a solution;
     *         <code>false</code> otherwise
     */
    private boolean solveBacktrack(int[][] emptyCells, int ind) {

        if (ind == emptyCells.length)
            return true;

        for (int trial = 1; trial <= sb.size; trial++) {
            if (isTrialValid(emptyCells[ind], trial)) {

                // If "trial" doesn't break any rule, insert it in the board and
                // move to the next unassigned cell.
                sb.set(trial, emptyCells[ind][0], emptyCells[ind][1]);

                if (solveBacktrack(emptyCells, ind + 1))
                    return true;

                // If "trial" finally doesn't lead to a solution, remove it and
                // try with "trial+1".
                sb.set(0, emptyCells[ind][0], emptyCells[ind][1]);
            }
        }
        return false;
    }
}