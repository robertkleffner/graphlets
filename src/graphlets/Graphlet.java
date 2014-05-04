package graphlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rob Kleffner
 */
public class Graphlet {
    public Node ItsCenter;
    public List<Node> ItsNeighbors;
    public List<Edge> ItsEdges;
    public List<Edge> ItsBoundaries;
    public Map<Graphlet, List<Integer>> ItsSharedNeighbors;
    public Map<Integer, Integer> ItsSharedMappings;
    
    public Graphlet() {
        ItsEdges = new ArrayList<>();
        ItsNeighbors = new ArrayList<>();
        ItsBoundaries = new ArrayList<>();
        ItsSharedNeighbors = new HashMap<>();
        ItsSharedMappings = new HashMap<>();
    }
    
    public String BoundaryLabel(int n1, int n2) {
        for (Edge e : ItsBoundaries) {
            if ((e.ItsFirstNode == n1 && e.ItsSecondNode == n2) ||
                    (e.ItsFirstNode == n2 && e.ItsSecondNode == n1)) {
                return e.ItsLabel;
            }
        }
        return null;
    }
    
    public boolean ContainsBoundary(int n1, int n2) {
        for (Edge e : ItsBoundaries) {
            if ((e.ItsFirstNode == n1 && e.ItsSecondNode == n2) ||
                    (e.ItsFirstNode == n2 && e.ItsSecondNode == n1)) {
                return true;
            }
        }
        return false;
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
    
    public int GetNodeDegree(int nodeIndex) {
        int degree = 0;
        int nodeId = ItsNeighbors.get(nodeIndex).ItsVertexId;
        for (Edge e: ItsBoundaries) {
            if (e.TouchesNode(nodeId))
                degree++;
        }
        return degree;
    }
    
    @Override
    public String toString() {
        return "Graphlet: <" + ItsCenter.ItsVertexId + ", " + ItsNeighbors + ", " + ItsBoundaries + ">";
    }
    
    public String SharedNeighborsInfo() {
        String result = "";
        for (Map.Entry<Graphlet, List<Integer>> e : ItsSharedNeighbors.entrySet()) {
            result += "Shared " + ItsCenter.ItsVertexId + ": " + e.getKey().ItsCenter.ItsVertexId + " " + e.getValue();
        }
        return result;
    }
    
    public static List<Map<Integer, Integer>> IsomorphicSubgraphMappings(Graphlet query, Graphlet data, boolean useLabels) {
        Map<Integer, Integer> mapping = new HashMap<>();
        
        List<List<Integer>> candidates = new ArrayList<>();
        for (Node n : query.ItsNeighbors) {
            candidates.add(FilterCandidates(n, data, useLabels));
            if (candidates.isEmpty()) {
                return new ArrayList<>();
            }
        }
        List<Map<Integer, Integer>> mappings = new ArrayList<>();
        SubgraphSearch(mappings, query, data, mapping, candidates, useLabels);
        return mappings;
    }
    
    private static List<Integer> FilterCandidates(Node n, Graphlet data, boolean useLabels) {
        List<Integer> possible;
        if (useLabels) {
            possible = new ArrayList<>();
            for (int i = 0; i < data.ItsNeighbors.size(); i++) {
                if (n.ItsLabel.equals(data.ItsNeighbors.get(i).ItsLabel)) {
                    possible.add(i);
                }
            }
        } else {
            possible = new ArrayList<>();
            for (int i = 0; i < data.ItsNeighbors.size(); i++) {
                possible.add(i);
            }
        }
        return possible;
    }
    
    private static void SubgraphSearch(List<Map<Integer, Integer>> mappings, Graphlet query, Graphlet data, Map<Integer,Integer> mapping, List<List<Integer>> candidates, boolean useLabels) {
        if (mapping.size() == query.ItsNeighbors.size()) {
            mappings.add(new HashMap<>(mapping));
            return;
        }
        
        for (int i = 0; i < candidates.size(); i++) {
            System.out.println(mapping.containsKey(i));
            if (mapping.containsKey(i))
                continue;
            System.out.println("Candidates: " + candidates.get(i));
            candidates.set(i, RefineCandidates(query, i, data, candidates.get(i)));
            System.out.println("Refined: " + candidates.get(i));
            for (Integer index : candidates.get(i)) {
                if (IsJoinable(query, data, mapping, i, index, useLabels)) {
                    mapping.put(i, index);
                    SubgraphSearch(mappings, query, data, mapping, candidates, useLabels);
                    mapping.remove(i);
                }
            }
        }
    }
    
    private static boolean IsJoinable(Graphlet query, Graphlet data, Map<Integer, Integer> mapping, int u, int v, boolean useLabels) {
        for (Map.Entry<Integer, Integer> e : mapping.entrySet()) {
            boolean containsU = query.ContainsBoundary(query.ItsNeighbors.get(u).ItsVertexId, query.ItsNeighbors.get(e.getKey()).ItsVertexId);
            boolean containsV = data.ContainsBoundary(data.ItsNeighbors.get(v).ItsVertexId, data.ItsNeighbors.get(e.getValue()).ItsVertexId);
            if (containsU && !containsV) {
                return false;
            } else if (containsU && containsV && useLabels) {
                if (!query.BoundaryLabel(query.ItsNeighbors.get(u).ItsVertexId, query.ItsNeighbors.get(e.getKey()).ItsVertexId).equals(
                    data.BoundaryLabel(data.ItsNeighbors.get(v).ItsVertexId, data.ItsNeighbors.get(e.getValue()).ItsVertexId)))
                    return false;
            }
        }
        return true;
    }
    
    private static List<Integer> RefineCandidates(Graphlet query, int index, Graphlet data, List<Integer> candidates) {
        int qdeg = query.GetNodeDegree(index);
        for (int i = 0; i < candidates.size(); i++) {
            if (qdeg > data.GetNodeDegree(candidates.get(i))) {
                candidates.remove(i);
                i--;
            }
        }
        return candidates;
    }
}
