package graphlets;

public class Node {
    public int ItsVertexId;
    public String ItsLabel;
    
    public Node(int vid, String label) {
        ItsVertexId = vid;
        ItsLabel = label;
    }
    
    @Override
    public String toString() {
        return "" + ItsVertexId;
    }
}
