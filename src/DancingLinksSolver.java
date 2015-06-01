/**
 * The DancingLinksSolver converts a sudoko board to a doubly-linked list and
 * solves it using Donald E. Knuth's Dancing Links algorithm.
 **/
public class DancingLinksSolver extends Solver {
    HeaderNode root;
    Node[] solution;
    HeaderNode firstColumn;
    int verbose = 0;

    public DancingLinksSolver(String[] input, String name) {
        super(input, name);
        init2DArray(input);
    }

    public void solve() {
        // initialize the doubly-linked list
        LinkedListBuilder builder = new LinkedListBuilder();
        root = builder.buildLinkedList(board);

        solution = new Node[100];

        // search and print all exact covers
        firstColumn = (HeaderNode)root.right;
        //while (startColumn != root) {
            search(0);
        //    startColumn = (HeaderNode)startColumn.right;
        //}
    }

    @Override
    public void print(String message) {
        if (solution != null) {
            for (Node o : solution) {
                if (o == null) break;
                String[] val = o.name.split("-");
                board[Integer.parseInt(val[0])][Integer.parseInt(val[1])]
                    = Integer.parseInt(val[2]);
            }
        }
        super.print(message);
    }

    void search(int k) {
        if (root.right == root) {
            printSolution();
            return;
        }
        HeaderNode c;
        if (k == 0)
            c = firstColumn;
        else
            c = chooseColumn();

        cover(c);
        for (Node i = c.down; !i.equals(c); i = i.down) {
            if (k > solution.length - 1) {
                Node[] tmp = new Node[k + 100];
                System.arraycopy(solution, 0, tmp, 0, solution.length);
                solution = tmp;
            }
            solution[k] = i;
            for (Node j = i.right; !j.equals(i); j = j.right) {
                cover(j.column);
            }
            search(k + 1);
            i = solution[k];
            c = i.column;
            for (Node j = i.left; !j.equals(i); j = j.left) {
                uncover(j.column);
            }
        }
        uncover(c);
    }

    /**
     * choose a column to cover; the current implementation returns the column
     * with minimum number of nodes, thus minimizing the branching factor.
     **/
    HeaderNode chooseColumn() {
        //return (HeaderNode)root.right;
        StringBuilder sb = new StringBuilder();
        HeaderNode minHeader = null;
        int minSize = Integer.MAX_VALUE;
        HeaderNode h = (HeaderNode)root.right;
        while (!h.equals(root)) {
            sb.append(h.name + " ");
            if (h.size > 0 && h.size < minSize) {
                minSize = h.size;
                minHeader = h;
            }
            h = (HeaderNode)h.right;
        }
        if (minHeader == null)
            minHeader = (HeaderNode)root.right;
        if (verbose == 1)
            System.out.println("Choosing column " + minHeader.name
                + "(" + minHeader.size + "): " + sb.toString());
        return minHeader;
    }

    void cover(HeaderNode c) {
        if (verbose == 1)
            System.out.println("Covering column " + c.name);
        c.right.left = c.left;
        c.left.right = c.right;
        for (Node i = c.down; !i.equals(c); i = i.down) {
            for (Node j = i.right; !j.equals(i); j = j.right) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.column.size--;
            }
        }
    }

    void uncover(HeaderNode c) {
        if (verbose == 1)
            System.out.println("Uncovering column " + c.name);
        for (Node i = c.up; !i.equals(c); i = i.up) {
            for (Node j = i.left; !j.equals(i); j = j.left) {
                j.column.size++;
                j.down.up = j;
                j.up.down = j;
            }
        }
        c.right.left = c;
        c.left.right = c;
    }
}
