import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class LinkedListBuilder {
    /**
     * Takes a sudoku board in two-dimensional integer array format and
     * converts it to a doubly-linked list suitable for applying the
     * Dancing Links algorithm.
     **/
    HeaderNode buildLinkedList(int[][] input) {
        // create a two-dimensional array of data objects. The array
        // has a column for each constraint and a row for each possible
        // number in each possible position.
        int size = input.length;
        HeaderNode[] header = new HeaderNode[size * size * 4];
        createHeaderNodes(size, header);

        Node[][] data = new Node[size * size * size + 1][size * size * 4];
        createDataNodes(input, data);

        // (for debugging) write to file
        writeToFile(header, data, "sudoku.csv");

        // now link the nodes to create the doubly-linked list
        linkDataNodes(header, data);
        HeaderNode root = linkHeaderNodes(header, data);

        verifyLinkedList(root, input);
        return root;
    }

    /**
     * Takes an input grid of the sudoku board in two-dimensional integer
     * array format and converts it to a two-dimensional array representing
     * possible positions as rows and their corresponding constraints as
     * columns. This method creates only the column header nodes where each
     * column represents a constraint.
     */
    void createHeaderNodes(int size, HeaderNode[] header) {
        int col = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                StringBuilder sb = new StringBuilder();
                // create one constraint for each position
                sb.append("R").append(i + 1).append("C").append(j + 1);
                header[col] = new HeaderNode(sb.toString());
                col++;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // create one constraint for each row
                StringBuilder sb = new StringBuilder();
                sb.append(i + 1).append("R").append(j + 1);
                header[col] = new HeaderNode(sb.toString());
                col++;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // create one constraint for each column
                StringBuilder sb = new StringBuilder();
                sb.append(i + 1).append("C").append(j + 1);
                header[col] = new HeaderNode(sb.toString());
                col++;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // create one constraint for each region
                StringBuilder sb = new StringBuilder();
                sb.append(i + 1).append("S").append(j + 1);
                header[col] = new HeaderNode(sb.toString());
                col++;
            }
        }
    }

    /**
     * Takes an input grid of the sudoku board in two-dimensional
     * integer array format and converts it to a two-dimensional
     * array representing possible positions and their corresponding
     * constraints. The constraints that exist are represented by Node
     * data objects. They are not linked in this method.
     */
    void createDataNodes(int[][] input, Node[][] data) {
        int size = input.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (input[i][j] == 0) {
                    // add an entry for each possible number
                    for (int k = 0; k < size; k++) {
                        // rows are ordered 1R1C1,...1R1C9,1R2C1...1R9C9,2R1C1...2R9C9...9R9C9
                        // add data object for position
                        data[(k * size * size) + (i * size) + j][i * size + j]
                             = new Node(i, j, k + 1);
                        // add data object for row
                        data[(k * size * size) + (i * size) + j][size * size
                            + k * size + i] = new Node(i, j, k + 1);
                        // add data object for column
                        data[(k * size * size) + (i * size) + j][2 * size * size
                            + k * size + j] = new Node(i, j, k + 1);
                        // add data object for region
                        data[(k * size * size) + (i * size) + j][3 * size * size
                            + k * size + getRegion(i, j, size)]
                            = new Node(i, j, k + 1);
                    }
                }
                else {
                    // add data object for position
                    data[((input[i][j] - 1) * size * size) + (i * size)
                        + j][i * size + j] = new Node(i, j, input[i][j]);
                    // add data object for row
                    data[((input[i][j] - 1) * size * size) + (i * size)
                        + j][size * size + (input[i][j] - 1) * size + i]
                        = new Node(i, j, input[i][j]);
                    // add data object for column
                    data[((input[i][j] - 1) * size * size) + (i * size)
                        + j][2 * size * size + (input[i][j] - 1) * size + j]
                        = new Node(i, j, input[i][j]);
                    // add data object for region
                    data[((input[i][j] - 1) * size * size) + (i * size)
                        + j][3 * size * size + (input[i][j] - 1) * size
                        + getRegion(i, j, size)] = new Node(i, j, input[i][j]);
                }
            }
        }
    }

    /**
     * Links data nodes to create the row and column linked lists.
     */
    void linkDataNodes(HeaderNode[] header, Node[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] == null) continue;

                // link left data node
                int l = (j == 0 ? cols - 1 : j - 1);
                while (data[i][l] == null && l != j) {
                    l = (l == 0 ? cols - 1 : l - 1);
                }
                data[i][j].left = data[i][l];

                // link right data node
                int r = (j == cols - 1 ? 0 : j + 1);
                while (data[i][r] == null && r != j) {
                    r = (r == cols - 1 ? 0 : r + 1);
                }
                data[i][j].right = data[i][r];

                // link up data node
                if (i == 0) {
                    data[i][j].up = header[j];
                } else {
                    int u = i - 1;
                    while (u > 0 && data[u][j] == null) u--;
                    if (u == 0) {
                        data[i][j].up = header[j];
                    } else {
                        data[i][j].up = data[u][j];
                    }
                }

                // link down data node
                if (i == rows - 1) {
                    data[i][j].down = header[j];
                } else {
                    int d = i + 1;
                    while (d < rows - 1 && data[d][j] == null) d++;
                    if (d == rows - 1) {
                        data[i][j].down = header[j];
                    } else {
                        data[i][j].down = data[d][j];
                    }
                }
                // link to head of the column list
                data[i][j].column = header[j];
            }
        }
    }

    /**
     * Links root and header nodes to each other; sets column size.
     *
     */
    HeaderNode linkHeaderNodes(HeaderNode[] header, Node[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        int colSize = 0;
        for (int j = 0; j < header.length; j++) {
            for (int i = 0; i < rows; i++) {
                if (data[i][j] != null) colSize++;
            }
            header[j].size = colSize;
            colSize = 0;
        }
        HeaderNode root = new HeaderNode("root");
        HeaderNode prev = root;
        HeaderNode last = null;

        for (int j = 0; j < header.length; j++) {
            if (header[j].size == 0) continue;
            header[j].left = prev;
            prev = header[j];
            last = header[j];

            int u = rows - 1;
            while (u >= 0 && data[u][j] == null) u--;
            header[j].up = data[u][j];
            int d = 0;
            while (d < rows && data[d][j] == null) d++;
            header[j].down = data[d][j];

            header[j].column = header[j];

        }
        root.left = last;

        HeaderNode next = root;
        HeaderNode first = null;
        for (int j = header.length - 1; j >= 0; j--) {
            if (header[j].size == 0) continue;
            header[j].right = next;
            next = header[j];
            first = header[j];
        }
        root.right = first;
        return root;
    }

    /*
     * Returns an integer based on which region of the grid the row and column
     * is in; the regions are numbered zero to (size - 1) from left to right
     * and top to bottom with the top-left being zero and bottom-right being
     * (size - 1).
     */
    int getRegion(int row, int col, int size) {
        int regionSize = (int)java.lang.Math.sqrt(size);
        return ((row / regionSize) * regionSize + (col / regionSize));
    }

    /*
     * An optional method for writing the two-dimensional array representation
     * of the positions and constraints to an output file in comma-separated
     * format.
     */
    void writeToFile(HeaderNode[] header, Node[][] data, String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder sbHeader = new StringBuilder();
            for (int i = 0; i < header.length; i++) {
                sbHeader.append(header[i].name).append(",");
            }
            bw.write(sbHeader.toString());
            bw.newLine();
            for (int i = 0; i < data.length; i++) {
                StringBuilder sbRow = new StringBuilder();
                for (int j = 0; j < data[0].length; j++) {
                    if (data[i][j] == null) {
                        sbRow.append("0,") ;
                    } else {
                        sbRow.append("1,");
                    }
                }
                bw.write(sbRow.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException ioe) {
            System.out.println("Error writing to file.");
        }
    }

    boolean verifyLinkedList(HeaderNode root, int[][] input) {
        boolean isValid = true;
        Map<String, Node> nodeMap = new HashMap<String, Node>();
        Node colHeader = root.right;
        while (colHeader != root) {
            Node row = colHeader.down;
            while (row != colHeader) {
                nodeMap.put(row.name, row);
                row = row.down;
            }
            colHeader = colHeader.right;
        }
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                if (input[i][j] == 0) {
                    for (int k = 0; k < input.length; k++) {
                        String name = i + "-" + j + "-" + (k + 1);
                        Node n = nodeMap.get(name);
                        if (n == null) {
                            System.out.println("name " + name + "("
                                + input[i][j] + ") not found in linked list.");
                            isValid = false;
                        }
                    }
                } else {
                    String name = i + "-" + j + "-" + input[i][j];
                    Node n = nodeMap.get(name);
                    if (n == null) {
                        System.out.println("name " + name + "("
                                + input[i][j] + ") not found in linked list.");
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
    }
}