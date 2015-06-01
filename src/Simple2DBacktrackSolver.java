/**
 * The Simple2DBacktrackSolver class solves a sudoku board represented by a
 * two-dimensional array using a very simple back-tracking algorithm.
 **/
public class Simple2DBacktrackSolver extends Solver {

    public Simple2DBacktrackSolver(String[] input, String name) {
        super(input, name);
        init2DArray(input);
    }

    public void solve() {
	solve2D();
    }

    /**
     * attempts to solve a sudoku board represented by a two-dimensional array;
     * returns true if the board is solved and false otherwise.
     *
     * This is a recursive procedure that attempts to place a number (1 - size)
     * in an empty slot and verifies if the position is valid. If
     * it is, it proceeds to the next empty slot. Otherwise, it tries again by
     * incrementing the number. When all possible numbers are tried for the
     * position, it backtracks to the previous position and tries by
     * incrementing the number.
     *
     * The selection of an empty slot in this implementation is quite
     * brain-dead, it progresses from top-left to bottom-right of the array.
     * The numbers 1 - size are tried sequentionally. The position is validated
     * on request, against the other entries in the row, column and region.
     *
     * The total number of iterations may be minimized by using a smarter
     * selection logic and using additional storage, e.g. boolean arrays for
     * validating the position.
     *
     **/
    boolean solve2D() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    while (board[i][j] < size) {
                        board[i][j]++;
                        if (isPositionValid(i, j)) {
	                    boolean isSolved = solve2D();
		            if (isSolved) printSolution();
                        }
                    }
                    board[i][j] = 0;
                    return false;
                }
            }
        }
	return true;
    }

    /**
     * returns true if the current position is valid, i.e., conforms to sudoku
     * rules that only one occurrence of a given number is allowed in a given
     * row, column and region.
     * A region is a sqrt(n) * sqrt(n) mini-grid within the n * n board.
     *
     **/
    boolean isPositionValid(int i, int j) {
        // check row
        for (int k = 0; k < size; k++) {
            if (board[i][j] == board[i][k] && k != j) {
                // row check failed
                return false;
            }
        }
        // check column
        for (int k = 0; k < size; k++) {
            if (board[i][j] == board[k][j] && k != i) {
                // column check failed
                return false;
            }
        }
        // check region
	for (int x = (i / rSize) * rSize; x < ((i / rSize) + 1) * rSize; x++) {
            for (int y = (j / rSize) * rSize; y < ((j / rSize) + 1) * rSize; y++) {
                if (board[i][j] == board[x][y] && (i != x || j != y)) {
                    // region check failed
                    return false;
                }
            }
	}
        // board is valid
        return true;
    }
}