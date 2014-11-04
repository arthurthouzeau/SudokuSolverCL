package sudoku;

import java.io.IOException;

/**
 * This class contains the main program. It loads a Sudoku from a CSV file,
 * solves it, and writes the results back to a CSV file. This class also 
 * handles a part of the exceptions and the comunication with the user.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
public class Main {

    /**
     * Check the number of input arguments and returns the pathname of the CSV
     * file where to write the results.
     * 
     * @param args command-line arguments, should be "inputFile [outputFile]"
     * @return the name of the output file, or <code>null</code> if the number
     *         of arguments is incorrect
     */
    private static String getOutputFile(String[] args) {
        if (args.length != 2 && args.length != 1) {
            System.err.println("Proper usage at the command-line is "
                            + "\"java -jar SudokuSolver inputFile [outputFile]\"");
            return null;
        }
        if (args.length == 2)
            return args[1];
        if (!args[0].contains("."))
            return args[0] + "_solved.csv";
        return args[0].substring(0, args[0].lastIndexOf('.')) + "_solved.csv";
    }

    /**
     * Loads a Sudoku from a CSV file, solves it, and writes the results back 
     * to a CSV file. This method also handles a part of the exceptions and 
     * the comunication with the user.
     * 
     * @param args command-line arguments, should be "inputFile [outputFile]"
     * @throws IOException if an IOException exception occured (except
     *                     FileNotFoundException)
     */
    public static void main(String[] args) throws IOException {

        // Check input arguments
        String outputFile = getOutputFile(args);
        if (outputFile == null)
            return;
        String inputFile = args[0];

        // Load CSV file
        SudokuBoard sb = SudokuBoard.loadFromCSV(inputFile);
        if (sb == null) {
            System.err.println("An error occured in the loading of the csv file.");
            System.err.println("Process stopped.");
            return;
        }

        // Check that SudokuBoard respects Sudoku rules
        if (!sb.checkInitialBoard()) {
            System.err.println("The initial grid seems incorrect.");
            System.err.println("Please check that :");
            System.err.println("- integers are between 0 and " + sb.size);
            System.err.println("- each integer appears only once on each row,"
                    + " column and region.");
            return;
        }

        // Solve
        RecursiveSolver rs = new RecursiveSolver(sb);
        if (!rs.solve()) {
            System.out.println("No solution.");
            return;
        }

        // Write the solution to the output CSV file
        rs.getBoard().writeToCSV(outputFile);
    }

}