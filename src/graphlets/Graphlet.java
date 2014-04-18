package graphlets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rob Kleffner
 */
public class Graphlet {
    public Node ItsCenter;
    public List<Node> ItsNeighbors;
    public List<Edge> ItsEdges;
    public List<Edge> ItsBoundaries;
    
    public Graphlet() {
        ItsEdges = new ArrayList<>();
        ItsNeighbors = new ArrayList<>();
        ItsBoundaries = new ArrayList<>();
    }
    
    public boolean ContainsEdge(int n1, int n2) {
        for (Edge e : ItsEdges) {
            if ((e.ItsFirstNode == n1 && e.ItsSecondNode == n2) ||
                    (e.ItsFirstNode == n2 && e.ItsSecondNode == n1)) {
                return true;
            }
        }
        for (Edge e : ItsBoundaries) {
            if ((e.ItsFirstNode == n1 && e.ItsSecondNode == n2) ||
                    (e.ItsFirstNode == n2 && e.ItsSecondNode == n1)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean HasNeighborId(int id) {
        for (Node n : ItsNeighbors) {
            if (n.ItsVertexId == id) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Graphlet: <" + ItsCenter.ItsVertexId + ", " + ItsNeighbors + ", " + ItsBoundaries + ">";
    }
}
