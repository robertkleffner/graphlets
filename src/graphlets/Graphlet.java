package graphlets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rob Kleffner
 */
public class Graphlet {
    public Node ItsCenter;
    public List<Node> ItsNeighbors;
    public List<Edge> ItsBoundaries;
    
    public Graphlet() {
        ItsNeighbors = new ArrayList<>();
        ItsBoundaries = new ArrayList<>();
    }
}
