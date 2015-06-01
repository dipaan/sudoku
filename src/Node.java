class Node {
    Node left;
    Node right;
    Node up;
    Node down;
    HeaderNode column; // points to the column header
    String name; // a string identifier for row name, for printing solution

    public Node(String name) {
        this.name = name;
    }
    int i;
    int j;
    int k;
    public Node(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.name = i + "-" + j + "-" + k;
    }
}
