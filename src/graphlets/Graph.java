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
    
    public Graph(List<Node> vertices, List<Edge> edges) {
        ItsVertices = new ArrayList<>(vertices);
        ItsEdges = new ArrayList<>(edges);
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
    
    public void RemoveEdge(Integer n1, Integer n2) {
        for (int i = 0; i < ItsEdges.size(); i++) {
            if ((ItsEdges.get(i).ItsFirstNode == n1 && ItsEdges.get(i).ItsSecondNode == n2) ||
                (ItsEdges.get(i).ItsFirstNode == n2 && ItsEdges.get(i).ItsSecondNode == n1)) {
                ItsEdges.remove(i);
                i--;
            }
        }
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
                for (List<Graphlet> gs : covers) {
                    for (int k = 0; k < gs.size(); k++) {
                        for (int j = k + 1; j < gs.size(); j++) {
                            ConstructSharedNeighbors(gs.get(k), gs.get(j), k, j);
                        }
                    }
                }
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
    
    private void ConstructSharedNeighbors(Graphlet g1, Graphlet g2, int i1, int i2) {
        for (int i = 0; i < g2.ItsNeighbors.size(); i++) {
            if (g1.HasNeighborId(g2.ItsNeighbors.get(i).ItsVertexId)) {
                if (!g1.ItsSharedNeighbors.containsKey(i2))
                    g1.ItsSharedNeighbors.put(i2, new ArrayList<Integer>());
                g1.ItsSharedNeighbors.get(i2).add(i);
            }
        }
        for (int i = 0; i < g1.ItsNeighbors.size(); i++) {
            if (g2.HasNeighborId(g1.ItsNeighbors.get(i).ItsVertexId)) {
                if (!g2.ItsSharedNeighbors.containsKey(i1))
                    g2.ItsSharedNeighbors.put(i1, new ArrayList<Integer>());
                g2.ItsSharedNeighbors.get(i1).add(i);
            }
        }
        
        if (g1.HasNeighborId(g2.ItsCenter.ItsVertexId)) {
            if (!g1.ItsSharedNeighbors.containsKey(i2))
                g1.ItsSharedNeighbors.put(i2, new ArrayList<Integer>());
            g1.ItsSharedNeighbors.get(i2).add(-1);
        }
        if (g2.HasNeighborId(g1.ItsCenter.ItsVertexId)) {
            if (!g2.ItsSharedNeighbors.containsKey(i1))
                g2.ItsSharedNeighbors.put(i1, new ArrayList<Integer>());
            g2.ItsSharedNeighbors.get(i1).add(-1);
        }
    }
}
