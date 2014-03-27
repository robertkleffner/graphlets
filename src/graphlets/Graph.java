package graphlets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rob Kleffner
 */
public class Graph {
    public List<Node> ItsVertices;
    public List<Edge> ItsEdges;
    
    public Graph() {
        ItsVertices = new ArrayList<>();
        ItsEdges = new ArrayList<>();
    }
    
    public Edge GetMappedEdge(Integer vertexOne, Integer vertexTwo) {
        for (Edge edge : ItsEdges) {
            if ((edge.ItsFirstNode == vertexOne && edge.ItsSecondNode == vertexTwo) ||
                    (edge.ItsSecondNode == vertexOne && edge.ItsSecondNode == vertexTwo)) {
                return edge;
            }
        }
        return null;
    }
    
    private static boolean Search(Graph search, Graph subgraph, List<List<Integer>> possible, List<Integer> assignments, List<Edge> edges) {
        int length = assignments.size();
        
        for (Edge edge : subgraph.ItsEdges) {
            if (edge.ItsFirstNode < length && edge.ItsSecondNode < length) {
                Edge graphe = search.GetMappedEdge(assignments.get(edge.ItsFirstNode), assignments.get(edge.ItsSecondNode));
                if (graphe == null)
                    return false;
                if (!edges.contains(graphe))
                    edges.add(graphe);
            }
        }
        
        if (length == subgraph.ItsVertices.size())
            return true;
        
        for (int i : possible.get(length)) {
            if (!assignments.contains(i)) {
                assignments.add(i);
                if (Search(search, subgraph, possible, assignments, edges))
                    return true;
                assignments.remove(assignments.size() - 1);
            }
        }
        
        return false;
    }
    
    public static Graph FindIsomorphism(Graph search, Graph subgraph) {
        List<Integer> assignments = new ArrayList<>();
        List<List<Integer>> possible = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < search.ItsVertices.size(); i++) {
            possible.add(new ArrayList<Integer>());
            for (Node n : search.ItsVertices) {
                possible.get(i).add(n.ItsVertexId);
            }
        }
        if (Search(search, subgraph, possible, assignments, edges)) {
            Graph g = new Graph();
            g.ItsEdges = edges;
            for (Node n : search.ItsVertices) {
                if (assignments.contains(n.ItsVertexId)) {
                    g.ItsVertices.add(n);
                }
            }
            return g;
        }
        return new Graph();
    }
}
