/**
 * The Simple1DBacktrackSolver class solves a sudoku board represented by a
 * one-dimensional array using a very simple back-tracking algorithm.
 **/
public class Simple1DBacktrackSolver extends Solver {
    int[] position;

    public Simple1DBacktrackSolver(String[] input, String name) {
        super(input, name);
        init1DArray(input);
    }

    public void solve() {
        solve1D(0);
    }

    boolean solve1D(int i) {
        while (i < position.length) {
            if (position[i] == 0) {
                while (position[i] < size) {
                    position[i]++;
                    if (isPositionValid(i)) {
                        boolean isSolved = solve1D(i + 1);
                        if (isSolved) printSolution();
                    }
                }
                position[i] = 0;
                return false;
            }
            i++;
        }
        return true;
    }

    boolean isPositionValid(int i) {
        // check row
        for (int x = (i / size) * size; x < (i / size) * size + size; x++) {
            if (position[x] == position[i] && x != i) return false;
        }
        // check column
        for (int x = i % size; x < position.length; x += size) {
            if (position[x] == position[i] && x != i) return false;
        }
        // check region:
        // to validate only one occurrence of a number exists in a given region,
        // we first representing the position as a region coordinate {a, b},
        // where a = {0, ..., rSize - 1} and b = {0, ..., rSize - 1}
        int a = i / (size * rSize);
        int b = (i % size) / rSize;

        // k represents the top-left-most position of the given region
        int k = a * size * rSize + b * rSize;

        // for each row within the region
        for (int n = 0; n < rSize; n++) {
            // for each column within the region
            for (int x = k + n * size; x < k + n * size + rSize; x++) {
                if (position[x] == position[i] && x != i) return false;
            }
        }

        return true;
    }

    void init1DArray(String[] input) {
        position = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            position[i] = Integer.parseInt(input[i]);
        }
    }

    /**
     * prints the current status of the board to sysout
     **/
    @Override
    public void print(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(message).append(":\n");

        for (int i = 0; i < size; i++) {
            sb.append("|");
            for (int j = 0; j < size; j++) {
                sb.append(position[i* size + j]).append("|");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
}
