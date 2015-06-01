/**
 * Base class for all solving algorithms; the specific solving strategy or
 * algorithm is implemented in the sub-class by implementing the solve  method.
 */
public abstract class Solver {
    int[][] board; // maintains current selections of the board
    int size; // length/breadth of the board
    int rSize; // length/breadth of the region/box
    long startTime;
    long endTime;
    int solutionCount;
    String name; // name of the solving strategy/algorithm

    public Solver(String[] input, String name) {
        size = (int)java.lang.Math.sqrt(input.length);
	rSize = (int)java.lang.Math.sqrt(size);
        this.name = name;
    }

    /**
     * initializes a 2D integer array representing the sudoku board from the
     * input String array.
     *
     * @param input
     */
    void init2DArray(String[] input) {
        board = new int[size][size];
        for (int i = 0; i < size * size; i++) {
            if (!input[i].equals("0")) {
                board[i / size][i % size] = Integer.parseInt(input[i]);
            }
        }
    }

    /**
     * sub-classes implement the solving algorithm
     */
    public abstract void solve();

    public void printInput() {
        print(name + " Input");
    }

    public void printSolution() {
        print(name + " Solution " + ++solutionCount);
    }

    /**
     * prints the current status of the board to sysout
     **/
    public void print(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(message).append(":\n");

        for (int i = 0; i < size; i++) {
            sb.append("|");
            for (int j = 0; j < size; j++) {
                sb.append(board[i][j]).append("|");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

}