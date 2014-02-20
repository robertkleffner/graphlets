package graphlets;

public class Edge {
    public int ItsEdgeId;
    public int ItsFirstNode;
    public int ItsSecondNode;
    public String ItsLabel;
    
    public Edge(int id, int first, int second, String label) {
        ItsEdgeId = id;
        ItsFirstNode = first;
        ItsSecondNode = second;
        ItsLabel = label;
    }
}
