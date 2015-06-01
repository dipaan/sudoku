/**
 * Accepts a sudoku challege as a command-line input and prints to sysout all
 * available solutions. The input board is a comma-delimited list of numbers,
 * row by row, with empty positions denoted by zeroes.
 **/
public class Sudoku {

    public static void main(String[] args) {
        String[] input = args[0].split(",");

	Solver solver;
        long startTime;

        solver = new Simple2DBacktrackSolver(input, "Simple2DBacktrackSolver");
        solver.printInput();
        startTime = System.currentTimeMillis();
        solver.solve();
        System.out.println("timeTaken by Simple2DBacktrackSolver: "
            + (System.currentTimeMillis() - startTime));

        solver = new Simple1DBacktrackSolver(input, "Simple1DBacktrackSolver");
        solver.printInput();
        startTime = System.currentTimeMillis();
        solver.solve();
        System.out.println("timeTaken by Simple1DBacktrackSolver: "
            + (System.currentTimeMillis() - startTime));

        solver = new DancingLinksSolver(input, "DancingLinksSolver");
        solver.printInput();
        startTime = System.currentTimeMillis();
        solver.solve();
        System.out.println("timeTaken by DancingLinksSolver: "
            + (System.currentTimeMillis() - startTime));
    }
}

/*
Example inputs:
java Sudoku 3,4,1,0,0,2,0,0,0,0,2,0,0,1,4,3
java Sudoku 0,0,5,0,4,0,2,0,8,9,0,0,0,2,0,0,0,6,8,0,0,1,0,9,0,0,0,0,0,0,0,0,4,6,0,0,4,0,7,0,0,0,9,0,5,0,0,3,6,0,0,0,0,0,0,0,0,7,0,0,0,0,9,0,0,0,0,3,0,0,0,1,7,0,2,0,6,0,4,0,0
java Sudoku 0,0,5,0,0,0,2,0,8,9,0,0,0,2,0,0,0,6,8,0,0,1,0,9,0,0,0,0,0,0,0,0,4,6,0,0,4,0,7,0,0,0,9,0,5,0,0,3,6,0,0,0,0,0,0,0,0,7,0,0,0,0,9,0,0,0,0,3,0,0,0,1,7,0,2,0,6,0,4,0,0
java Sudoku 4,0,1,0,6,0,7,0,0,3,0,0,0,0,0,0,0,2,5,0,0,4,0,7,0,0,6,0,0,0,3,0,8,0,5,0,9,0,0,0,0,0,0,0,7,0,4,0,1,0,5,0,0,0,0,0,0,9,0,1,0,0,4,2,0,0,0,0,0,0,0,8,0,0,4,0,5,0,2,9,0
java Sudoku 0,0,3,0,0,0,9,0,0,0,0,0,7,0,3,5,6,0,0,0,0,1,0,0,0,3,8,0,4,9,0,0,0,0,0,0,5,0,0,0,0,0,0,0,2,0,0,0,0,0,0,6,8,0,4,2,0,0,0,5,0,0,0,0,9,8,3,0,6,0,7,0,0,0,7,0,0,0,0,0,0
*/
