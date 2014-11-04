# SudokuSolverCL

Author: Arthur Thouzeau  
Date: 2nd November 2014

## Overview

SudokuSolverCL is a java program running at the command-line that solves 
Sudoku puzzles **from different sizes** using the Recursive Backtracking 
method. It loads a Sudoku from a CSV file, tries to solve it, and writes 
the results to a different CSV file if a solution is found.

## How to use

First make sure that your input CSV containing the unsolved Sudoku respects the following format:
- values must be comma-separated, without quotes, but can have whitespaces between them
- unassigned values must be 0s
- the grid must be a square grid respecting basic Sudoku rules
- there is no practical limitation on the size of the grid, as long as it is a perfect square. (It can be 4, 9, 16, 25...)

Then import the project in a Java IDE like Eclipse. You can either run the program from your IDE or export the project into a runnable JAR file and then run the program at the command-line:

> java -jar runnable_name.jar inputPathName [outputPathName]

This program takes two arguments:

1. **inputPathName**: mandatory, this is the pathname of the CSV file where to read the unsolved Sudoku
2. **outputPathName**: optional, this is the pathname of the CSV file where to write the results if a solution is found. If this argument is omitted, the outputPathName will be the inputPathName appended with "_solved"

## Documentation

See the Javadoc.

## Notes

This program uses the OpenCSV library for reading and writing CSV files.
Please the "Notes" document for some explanations on the choices of implementations.
