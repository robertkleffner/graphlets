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
    
    public List<Graphlet> GetUnstructuredMatchingGraphlets(Graphlet g) {
        List<Graphlet> nodes = new ArrayList<>();
        try {
            Statement s = _connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM APP.node v "
                    + "WHERE v.label = '" + g.ItsCenter.ItsLabel + "' "
                    + "AND (SELECT COUNT (*) FROM APP.edge WHERE firstNode = v.vertexId OR secondNode = v.vertexId) >= " + g.ItsNeighbors.size() + " "
                    + "AND (SELECT COUNT (*) FROM APP.boundary WHERE vertexId = v.vertexId) >= " + g.ItsBoundaries.size() + " ");
            while (rs.next()) {
                nodes.add(SelectGraphlet(rs.getInt("vertexId"), rs.getString("label")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nodes;
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
        System.out.println("\tNeighbors: " + g.ItsNeighbors.size());
        System.out.println("\tBoundaries: " + g.ItsBoundaries.size());
        return g;
    }
}
