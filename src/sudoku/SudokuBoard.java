package sudoku;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * This class represents a Sudoku board as a two-dimensional
 * <code>int<code> array. It provides methods to load a Sudoku 
 * from a CSV file, write a Sudoku to a CSV file, and check the 
 * correctness of an initial grid.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
public class SudokuBoard {
    final int size;
    private int[][] board;

    /**
     * Initializes a new SudokuBoard.
     * 
     * @param size the number of rows (or columns) of the new SudokuBoard
     */
    private SudokuBoard(int size) {
        this.size = size;
        this.board = new int[size][size];
    }

    /**
     * Sets the value of a SudokuBoard's cell.
     * 
     * @param value the value to insert
     * @param row the cell's row
     * @param col the column's row
     */
    public void set(int value, int row, int col) {
        board[row][col] = value;
    }

    /**
     * Returns the value of a SudokuBoard's cell.
     * 
     * @param row the cell's row
     * @param col the column's row
     * @return the value of the cell
     */
    public int get(int row, int col) {
        return board[row][col];
    }

    /**
     * Check that each Sudoku cell's value is between 0 and the size of the
     * SudokuBoard (inclusive).
     * 
     * @return <code>true</code> if all cells are in the correct range;
     *         <code>false</code> otherwise
     */
    private boolean checkInitialRange() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] < 0 || board[i][j] > size)
                    return false;
            }
        }
        return true;
    }

    /**
     * Check that numbers between 1 and the size of the SudokuBoard (inclusive)
     * appear only once on each row. This method must be called after checking
     * the values' range.
     * 
     * @return <code>true</code> if the row constraint is respected;
     *         <code>false</code> otherwise
     */
    private boolean checkInitialRows() {
        BitSet bits = new BitSet(size + 1);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0 && bits.get(board[i][j]))
                    return false;
                bits.set(board[i][j]);
            }
            bits.clear();
        }
        return true;
    }

    /**
     * Check that numbers between 1 and the size of the SudokuBoard (inclusive)
     * appear only once on each column. This method must be called after
     * checking the values' range.
     * 
     * @return <code>true</code> if the column constraint is respected;
     *         <code>false</code> otherwise
     */
    private boolean checkInitialCols() {
        BitSet bits = new BitSet(size + 1);

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                if (board[i][j] != 0 && bits.get(board[i][j]))
                    return false;
                bits.set(board[i][j]);
            }
            bits.clear();
        }
        return true;
    }

    /**
     * Check that numbers between 1 and the size of the SudokuBoard (inclusive)
     * appear only once on each box. This method must be called after checking
     * the values' range.
     * 
     * @return <code>true</code> if the box constraint is respected;
     *         <code>false</code> otherwise
     */
    private boolean checkInitialBoxes() {
        BitSet bits = new BitSet(size + 1);
        int sqrtSize = (int) Math.sqrt(size);

        for (int i = 0; i < size; i += sqrtSize) {
            for (int j = 0; j < size; j += sqrtSize) {
                for (int k = 0; k < sqrtSize; k++) {
                    for (int l = 0; l < sqrtSize; l++) {
                        if (board[i + k][j + l] != 0
                                && bits.get(board[i + k][j + l]))
                            return false;
                        bits.set(board[i + k][j + l]);
                    }
                }
                bits.clear();
            }
        }
        return true;
    }

    /**
     * Check that <code>this</code> initial board respects the Sudoku rules.
     * 
     * @return <code>true</code> if the board respects the rules of Sudoku;
     *         <code>false</code> otherwise
     */
    public boolean checkInitialBoard() {
        return (checkInitialRange() 
                && checkInitialRows() 
                && checkInitialCols() 
                && checkInitialBoxes());
    }

    /**
     * Static factory method returning an instance of class SudokuBoard, by
     * loading a Sudoku from a CSV file. This method performs several checks to
     * ensure the Sudoku provided is correct.
     * 
     * @param inputFile the pathname of the CSV file where to read the Sudoku
     * @return the loaded SudokuBoard, or <code>null</code> if a problem occured
     *         while trying to read the input CSV
     * @throws IOException if an IOException exception occured (except
     *                     FileNotFoundException)
     */
    public static SudokuBoard loadFromCSV(String inputFile) throws IOException {

        // Declare the CSVReader instance in a try-with-resource statement
        try (CSVReader reader = new CSVReader(new FileReader(inputFile))) {
            String[] nextLine;
            int countLines = 0;

            // Read first line and check that the file is not empty
            if ((nextLine = reader.readNext()) == null) {
                System.err.println("Please check that the file is not empty.");
                return null;
            }

            // Check that the number of columns is a perfect square.
            int tst = (int) Math.sqrt(nextLine.length);
            if (tst * tst != nextLine.length) {
                System.err.println("Please check the number of columns");
                System.err.println("It should be a perfect square.");
                return null;
            }

            // Initialize SudokuBoard and read CSV line by line to populate it
            SudokuBoard sb = new SudokuBoard(nextLine.length);
            do {
                if (nextLine.length != sb.size) {
                    System.err.println("Please check each row has " + sb.size
                            + " columns. (Same as first row)");
                    return null;
                }
                for (int j = 0; j < nextLine.length; j++) {
                    // trim() is used here to make the program more robust, as
                    // CSV files may have whitespaces between values
                    sb.set(Integer.parseInt(nextLine[j].trim()), countLines, j);
                }
                countLines++;
            } while ((nextLine = reader.readNext()) != null
                    && countLines < sb.size);

            // Check there are as many rows as columns
            if (countLines < sb.size || nextLine != null) {
                System.err.println("Please check the input CSV file has "
                        + sb.size + " rows. (To match the number of columns)");
                return null;
            }
            ;
            return sb;
        } catch (FileNotFoundException e) {
            System.err.println("The file you specified was not found.");
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Please check all values are integer.");
            return null;
        }
    }

    /**
     * Writes <code>this</code> board to a CSV file.
     * 
     * @param outputFile the pathname of the CSV file where to write the results
     * @throws IOException if an IOException exception occured (except
     *                     FileNotFoundException)
     */
    public void writeToCSV(String outputFile) throws IOException {
        String[][] output = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                output[i][j] = String.valueOf(get(i, j));
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',',
                CSVWriter.NO_QUOTE_CHARACTER)) {

            for (String[] record : output)
                writer.writeNext(record);
            System.out.println("The file has been written to: " + outputFile);
        } catch (FileNotFoundException e) {
            System.err.println("An error occured while writing the file.");
            System.err.println("Please check the path is correct, "
                    + "or that the destination file is not protected.");
        }
    }

}
