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
    
    public Node GetNodeById(int id) {
        for (Node n : ItsVertices) {
            if (id == n.ItsVertexId) {
                return n;
            }
        }
        return null;
    }
    
    public boolean ContainsEdge(int n1, int n2) {
        for (Edge x: ItsEdges) {
            if ((x.ItsFirstNode == n1 && x.ItsSecondNode == n2) ||
                    (x.ItsFirstNode == n2 && x.ItsSecondNode == n1)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean ContainsEdge(Edge e) {
        for (Edge x: ItsEdges) {
            if ((x.ItsFirstNode == e.ItsFirstNode && x.ItsSecondNode == e.ItsSecondNode) ||
                    (x.ItsFirstNode == e.ItsSecondNode && x.ItsSecondNode == e.ItsFirstNode)) {
                return true;
            }
        }
        return false;
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
    
    private static void Search(Graph search, Graph subgraph, List<List<Integer>> possible, List<Integer> assignments, List<Edge> edges, List<Graph> matched) {
        int length = assignments.size();
        
        for (Edge edge : subgraph.ItsEdges) {
            if (edge.ItsFirstNode < length && edge.ItsSecondNode < length) {
                Edge graphe = search.GetMappedEdge(assignments.get(edge.ItsFirstNode), assignments.get(edge.ItsSecondNode));
                if (graphe == null)
                    return;
                if (!edges.contains(graphe))
                    edges.add(graphe);
            }
        }
        
        if (length == subgraph.ItsVertices.size()) {
            Graph g = new Graph();
            for (Node n : search.ItsVertices) {
                if (assignments.contains(n.ItsVertexId)) {
                    g.ItsVertices.add(n);
                }
            }
            for (Edge edge : subgraph.ItsEdges) {
                Edge graphe = search.GetMappedEdge(assignments.get(edge.ItsFirstNode), assignments.get(edge.ItsSecondNode));
                g.ItsEdges.add(graphe);
            }
            for (Graph x: matched) {
                if (x.ItsEdges.size() != g.ItsEdges.size()) {
                    continue;
                }
                boolean contains = true;
                for (Edge e1: x.ItsEdges) {
                    if (!g.ContainsEdge(e1)) {
                        contains = false;
                        break;
                    }
                }
                if (contains) {
                    return;
                }
            }
            matched.add(g);
            return;
        }
        
        for (int i : possible.get(length)) {
            if (!assignments.contains(i)) {
                assignments.add(i);
                Search(search, subgraph, possible, assignments, edges, matched);
                assignments.remove(assignments.size() - 1);
            }
        }
    }
    
    public static List<Graph> FindIsomorphisms(Graph search, Graph subgraph) {
        List<Integer> assignments = new ArrayList<>();
        List<List<Integer>> possible = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<Graph> matched = new ArrayList<>();
        for (int i = 0; i < search.ItsVertices.size(); i++) {
            possible.add(new ArrayList<Integer>());
            for (Node n : search.ItsVertices) {
                possible.get(i).add(n.ItsVertexId);
            }
        }
        Search(search, subgraph, possible, assignments, edges, matched);
        return matched;
    }
    
    public List<List<Graphlet>> GetMinimalCandidateHubCovers() {
        List<List<Graphlet>> covers = new ArrayList<>();
        for (int i = 1; i < this.ItsVertices.size(); i++) {
            List<List<Integer>> combs = GetVertexCombinations(this.ItsVertices, i);
            for (List<Integer> c : combs) {
                List<Graphlet> gs = GetGraphletsByIds(c);
                List<Edge> test = new ArrayList<>(ItsEdges);
                for (int j = 0; j < test.size(); j++) {
                    for (Graphlet g : gs) {
                        if (g.ContainsEdge(test.get(j).ItsFirstNode, test.get(j).ItsSecondNode)) {
                            test.remove(j);
                            j--;
                            if (j < 0) {
                                break;
                            }
                        }
                    }
                }
                if (test.size() < 1) {
                    covers.add(gs);
                }
            }
            if (!covers.isEmpty()) {
                for (List<Graphlet> g : covers) {
                    System.out.println(g);
                }
                return covers;
            }
        }
        return null;
    }
    
    public List<Graphlet> GetGraphletsByIds(List<Integer> ids) {
        List<Graphlet> graphlets = new ArrayList<>();
        for (Integer i : ids) {
            Graphlet g = new Graphlet();
            g.ItsCenter = GetNodeById(i);
            for (Edge e : this.ItsEdges) {
                if (e.ItsFirstNode == g.ItsCenter.ItsVertexId) {
                    g.ItsEdges.add(e);
                    g.ItsNeighbors.add(GetNodeById(e.ItsSecondNode));
                } else if (e.ItsSecondNode == g.ItsCenter.ItsVertexId) {
                    g.ItsEdges.add(e);
                    g.ItsNeighbors.add(GetNodeById(e.ItsFirstNode));
                }
            }
            for (Edge e: this.ItsEdges) {
                if (g.HasNeighborId(e.ItsFirstNode) && g.HasNeighborId(e.ItsSecondNode)) {
                    g.ItsBoundaries.add(e);
                }
            }
            graphlets.add(g);
        }
        return graphlets;
    }
    
    public List<List<Integer>> GetVertexCombinations(List<Node> values, int ncombs) {
        List<Node> data = new ArrayList<>();
        for (int i = 0; i < ncombs; i++) {
            data.add(new Node(0, ""));
        }
        List<List<Integer>> build = new ArrayList<>();
        combinationUtil(values, data, build, 0, values.size() - 1, 0, ncombs);
        return build;
    }
    
    private void combinationUtil(List<Node> values, List<Node> data, List<List<Integer>> build, int start, int end, int index, int ncombs) {
        if (index == ncombs) {
            List<Integer> ids = new ArrayList<>();
            for (Node n : data) {
                ids.add(n.ItsVertexId);
            }
            build.add(ids);
            return;
        }
        
        for (int i = start; i <= end && end-i+1 >= ncombs-index; i++) {
            data.set(index, values.get(i));
            combinationUtil(values, data, build, i+1, end, index+1, ncombs);
        }
    }
}
