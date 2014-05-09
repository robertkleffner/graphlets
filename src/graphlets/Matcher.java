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
        
        List<Graph> result = new ArrayList<>();
        //GetMapping(isomappings, new HashMap<Integer, Integer>(), new Graph(), cover, new ArrayList<Integer>(), q, useLabel, query.ItsVertices.size());
        GetMapping(result, new Graph(), cover, new ArrayList<Integer>(), q, useLabel, query.ItsVertices.size(), query.ItsEdges.size());
        for (Graph g : result) {
            System.out.println(g.ItsVertices);
            System.out.println(g.ItsEdges);
        }
        
        return result;
    }
    
    private void GetMapping(List<Graph> full, Graph current, List<Graphlet> cover, List<Integer> searched, Graphlet q, boolean useLabel, int vertices, int edges) {
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
                List<Edge> newedge = new ArrayList<>();
                List<Edge> newbound = new ArrayList<>();
                for (Map.Entry<Integer, Integer> e : m.entrySet()) {
                    if (current.GetNodeById(d.ItsNeighbors.get(e.getValue()).ItsVertexId) == null) {
                        current.ItsVertices.add(d.ItsNeighbors.get(e.getValue()));
                        if (!current.ContainsEdge(d.GetEdge(d.ItsCenter.ItsVertexId, d.ItsNeighbors.get(e.getValue()).ItsVertexId))) {
                            current.ItsEdges.add(d.GetEdge(d.ItsCenter.ItsVertexId, d.ItsNeighbors.get(e.getValue()).ItsVertexId));
                            newedge.add(d.GetEdge(d.ItsCenter.ItsVertexId, d.ItsNeighbors.get(e.getValue()).ItsVertexId));
                        }
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
                if (current.ItsVertices.size() == vertices && current.ItsEdges.size() == edges) {
                    full.add(new Graph(current.ItsVertices, current.ItsEdges));
                } else {
                    for (Integer i : q.ItsSharedNeighbors.keySet()) {
                        if (searched.contains(cover.get(i).ItsCenter.ItsVertexId))
                            continue;
                        
                        GetMapping(full, current, cover, searched, cover.get(i), useLabel, vertices, edges);
                       
                    }
                }
                for (Integer i : added) {
                    current.ItsVertices.remove(d.ItsNeighbors.get(i));
                }
                for (Edge e : newedge) {
                    current.RemoveEdge(e.ItsFirstNode, e.ItsSecondNode);
                }
                for (Edge e : newbound) {
                    current.RemoveEdge(e.ItsFirstNode, e.ItsSecondNode);
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
        return g;
    }
}
