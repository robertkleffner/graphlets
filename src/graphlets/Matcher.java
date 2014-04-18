package graphlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        return null;
    }
    
    public List<Graph> GetMatchingGraphlets(Graphlet query, boolean useLabel) {
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
        
        Graph queryGraph = new Graph();
        queryGraph.ItsVertices.add(query.ItsCenter);
        for (Node n : query.ItsNeighbors) {
            queryGraph.ItsVertices.add(n);
        }
        for (Edge e : query.ItsEdges) {
            queryGraph.ItsEdges.add(e);
        }
        for (Edge e : query.ItsBoundaries) {
            queryGraph.ItsEdges.add(e);
        }
        
        queryGraph.GetMinimalCandidateHubCovers();
        return null;
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
