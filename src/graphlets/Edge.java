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
    
    public boolean TouchesNode(int node) {
        if (ItsFirstNode == node || ItsSecondNode == node)
            return true;
        return false;
    }
    
    @Override
    public String toString() {
        return "[" + ItsFirstNode + ", " + ItsSecondNode + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return false;
        if (!(obj instanceof Edge))
            return false;
        Edge other = (Edge)obj;
        return this.ItsFirstNode == other.ItsFirstNode && this.ItsSecondNode == other.ItsSecondNode;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.ItsFirstNode;
        hash = 17 * hash + this.ItsSecondNode;
        return hash;
    }
}
