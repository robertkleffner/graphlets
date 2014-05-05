package graphlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rob Kleffner
 */
public class Matcher {
    private final Connection _connection;
    
    public Matcher(Connection c) {
        _connection = c;
    }
    
    public List<Graph> GetMatchingGraphs(Graph query, boolean useLabel) {
        List<List<Graphlet>> covers = query.GetMinimalCandidateHubCovers();
        // right now, just use the first cover
        List<Graphlet> cover = covers.get(0);
        for (Graphlet g : cover) {
            System.out.println(g.SharedNeighborsInfo());
        }
        
        Graphlet q = cover.get(0);
        
        //List<Graphlet> matches = GetCandidateMatchingGraphlets(q, useLabel);
        //for (Graphlet g : matches) {
        //    List<Map<Integer, Integer>> mappings = Graphlet.IsomorphicSubgraphMappings(q, g, useLabel);
        //}
        // select all candidates for first graphlet
        // check for isomorphism
        // for each result, select neighboring graphlets containing shared vertices
            // if no result, eliminate entire subsearch
            // else check for isomorphism
        //
        
        List<Graph> result = new ArrayList<>();
        //GetMapping(isomappings, new HashMap<Integer, Integer>(), new Graph(), cover, new ArrayList<Integer>(), q, useLabel, query.ItsVertices.size());
        GetMapping(result, new Graph(), cover, new ArrayList<Integer>(), q, useLabel, query.ItsVertices.size());
        for (Graph g : result) {
            System.out.println(g.ItsVertices);
            System.out.println(g.ItsEdges);
        }
        
        return null;
    }
    
//    private void GetMapping(List<Map<Integer, Integer>> full, Map<Integer, Integer> current, List<Graph> fullg, Graph currentg, List<Graphlet> cover, List<Integer> searched, Graphlet q, boolean useLabel, int vertices) {
//        List<Graphlet> matches = GetCandidateMatchingGraphlets(q, useLabel);
//        for (Graphlet d : matches) {
//            current.put(q.ItsCenter.ItsVertexId, d.ItsCenter.ItsVertexId);
//            
//            List<Map<Integer, Integer>> mappings = Graphlet.IsomorphicSubgraphMappings(q, d, useLabel);
//            for (Map<Integer, Integer> m : mappings) {
//                List<Integer> added = new ArrayList<>();
//                for (Map.Entry<Integer, Integer> e : m.entrySet()) {
//                    if (!current.containsKey(q.ItsNeighbors.get(e.getKey()).ItsVertexId)) {
//                        current.put(q.ItsNeighbors.get(e.getKey()).ItsVertexId, d.ItsNeighbors.get(e.getValue()).ItsVertexId);
//                        added.add(q.ItsNeighbors.get(e.getKey()).ItsVertexId);
//                    }
//                }
//                if (current.size() == vertices) {
//                    full.add(current);
//                    return;
//                } else {
//                    for (Integer i : q.ItsSharedNeighbors.keySet()) {
//                        if (searched.contains(i))
//                            continue;
//                        searched.add(i);
//                        GetMapping(full, current, cover, searched, q, useLabel, vertices);
//                        searched.remove(i);
//                    }
//                }
//                for (Integer i : added) {
//                    current.remove(i);
//                }
//            }
//            
//            current.remove(q.ItsCenter.ItsVertexId);
//        }
//    }
    
    private void GetMapping(List<Graph> full, Graph current, List<Graphlet> cover, List<Integer> searched, Graphlet q, boolean useLabel, int vertices) {
        searched.add(q.ItsCenter.ItsVertexId);
        List<Graphlet> matches = GetCandidateMatchingGraphlets(q, useLabel);
        for (Graphlet d : matches) {
            boolean addedCenter = false;
            if (current.GetNodeById(d.ItsCenter.ItsVertexId) == null) {
                current.ItsVertices.add(d.ItsCenter);
                addedCenter = true;
            }
            
            List<Map<Integer, Integer>> mappings = Graphlet.IsomorphicSubgraphMappings(q, d, useLabel);
            for (Map<Integer, Integer> m : mappings) {
                List<Integer> added = new ArrayList<>();
                List<Edge> newbound = new ArrayList<>();
                for (Map.Entry<Integer, Integer> e : m.entrySet()) {
                    if (current.GetNodeById(d.ItsNeighbors.get(e.getValue()).ItsVertexId) == null) {
                        current.ItsVertices.add(d.ItsNeighbors.get(e.getValue()));
                        current.ItsEdges.add(d.GetEdge(d.ItsCenter.ItsVertexId, d.ItsNeighbors.get(e.getValue()).ItsVertexId));
                        for (Map.Entry<Integer, Integer> e2 : m.entrySet()) {
                            if (q.ContainsBoundary(q.ItsNeighbors.get(e.getKey()).ItsVertexId, q.ItsNeighbors.get(e2.getKey()).ItsVertexId) &&
                                !current.ContainsEdge(d.GetEdge(d.ItsNeighbors.get(e.getValue()).ItsVertexId, d.ItsNeighbors.get(e2.getValue()).ItsVertexId))) {
                                current.ItsEdges.add(d.GetEdge(d.ItsNeighbors.get(e.getValue()).ItsVertexId, d.ItsNeighbors.get(e2.getValue()).ItsVertexId));
                                newbound.add(d.GetEdge(d.ItsNeighbors.get(e.getValue()).ItsVertexId, d.ItsNeighbors.get(e2.getValue()).ItsVertexId));
                            }
                        }
                        added.add(e.getValue());
                    }
                }
                if (current.ItsVertices.size() == vertices) {
                    full.add(new Graph(current.ItsVertices, current.ItsEdges));
                    return;
                } else {
                    for (Integer i : q.ItsSharedNeighbors.keySet()) {
                        if (searched.contains(cover.get(i).ItsCenter.ItsVertexId))
                            continue;
                        
                        GetMapping(full, current, cover, searched, cover.get(i), useLabel, vertices);
                       
                    }
                }
                for (Integer i : added) {
                    current.ItsVertices.remove(d.ItsNeighbors.get(i));
                    current.ItsEdges.remove(d.GetEdge(d.ItsCenter.ItsVertexId, d.ItsNeighbors.get(i).ItsVertexId));
                }
                for (Edge e : newbound) {
                    current.ItsEdges.remove(e);
                }
            }
            
            if (addedCenter) {
                current.ItsVertices.remove(q.ItsCenter);
            }
            searched.remove((Integer)q.ItsCenter.ItsVertexId);
        }
    }
    
    public List<Graphlet> GetCandidateMatchingGraphlets(Graphlet query, boolean useLabel) {
        List<Graphlet> graphlets = new ArrayList<>();
        try {
            Statement s = _connection.createStatement();
            ResultSet rs;
            if (useLabel) {
                rs = s.executeQuery("SELECT * FROM APP.node v "
                    + "WHERE v.label = '" + query.ItsCenter.ItsLabel + "' "
                    + "AND (SELECT COUNT (*) FROM APP.edge WHERE firstNode = v.vertexId OR secondNode = v.vertexId) >= " + query.ItsNeighbors.size() + " "
                    + "AND (SELECT COUNT (*) FROM APP.boundary WHERE vertexId = v.vertexId) >= " + query.ItsBoundaries.size() + " ");
            } else {
                rs = s.executeQuery("SELECT * FROM APP.node v "
                    + "WHERE (SELECT COUNT (*) FROM APP.edge WHERE firstNode = v.vertexId OR secondNode = v.vertexId) >= " + query.ItsNeighbors.size() + " "
                    + "AND (SELECT COUNT (*) FROM APP.boundary WHERE vertexId = v.vertexId) >= " + query.ItsBoundaries.size() + " ");
            }
            while (rs.next()) {
                graphlets.add(SelectGraphlet(rs.getInt("vertexId"), rs.getString("label")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return graphlets;
        /*
        List<Graph> filtered = new ArrayList<>();
        for (Graphlet g : graphlets) {
            Graph subgraph = new Graph();
            subgraph.ItsVertices.add(g.ItsCenter);
            for (Node n : g.ItsNeighbors) {
                subgraph.ItsVertices.add(n);
            }
            for (Edge e : g.ItsEdges) {
                subgraph.ItsEdges.add(e);
            }
            for (Edge e : g.ItsBoundaries) {
                subgraph.ItsEdges.add(e);
            }
            List<Graph> result = Graph.FindIsomorphisms(subgraph, queryGraph);
            for (Graph res : result) {
                if (!res.ItsVertices.isEmpty()) {
                    filtered.add(res);
                }
            }
        }
        System.out.println("Matched: " + filtered.size() + " graphlet(s)");
        for (Graph g : filtered) {
            System.out.println("\tGraph:");
            System.out.println("\t\tVertices: " + g.ItsVertices);
            System.out.println("\t\tEdges: " + g.ItsEdges);
        }
        
        return filtered;
        */
    }
    
    private Graphlet SelectGraphlet(int vertexId, String label) {
        Graphlet g = new Graphlet();
        g.ItsCenter = new Node(vertexId, label);
        try {
            // first get neighbors
            Statement s = _connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM APP.node WHERE vertexId IN ("
                    + "(SELECT secondNode FROM APP.edge WHERE firstNode = " + vertexId + ") UNION DISTINCT "
                    + "(SELECT firstNode FROM APP.edge WHERE secondNode = " + vertexId + "))");
            while (rs.next()) {
                g.ItsNeighbors.add(new Node(rs.getInt("vertexId"), rs.getString("label")));
            }
            
            // get edges
            s = _connection.createStatement();
            rs = s.executeQuery("SELECT * FROM APP.edge WHERE firstNode = " + vertexId + " OR secondNode = " + vertexId);
            while (rs.next()) {
                g.ItsEdges.add(new Edge(rs.getInt("edgeId"), rs.getInt("firstNode"), rs.getInt("secondNode"), rs.getString("label")));
            }
            
            // next get boundaries
            s = _connection.createStatement();
            rs = s.executeQuery("SELECT * FROM APP.edge WHERE edgeId IN "
                    + "(SELECT edgeId FROM APP.boundary WHERE vertexId = " + vertexId + ")");
            while (rs.next()) {
                g.ItsBoundaries.add(new Edge(rs.getInt("edgeId"), rs.getInt("firstNode"), rs.getInt("secondNode"), rs.getString("label")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Graphlet: ");
        System.out.println("\tCenter ID: " + g.ItsCenter.ItsVertexId);
        System.out.println("\tEdges: " + g.ItsEdges.toString());
        System.out.println("\tNeighbors: " + g.ItsNeighbors.toString());
        System.out.println("\tBoundaries: " + g.ItsBoundaries.toString());
        return g;
    }
}
