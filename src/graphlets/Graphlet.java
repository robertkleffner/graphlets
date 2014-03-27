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
}
