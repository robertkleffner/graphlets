package graphlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Importer {
    private final String _dbUrl = "jdbc:derby:graphlets;create=true;user=app;password=app";
    public Connection ItsConnection;
    private List<Node> _nodes;
    private List<Edge> _edges;
    private int _edgeIdCounter;
    
    public void Connect() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            ItsConnection = DriverManager.getConnection(_dbUrl); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void Shutdown() {
        try {
            if (ItsConnection != null) {
                //DriverManager.getConnection(_dbUrl + ";shutdown=true");
                ItsConnection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ImportFile(String filename) {
        _nodes = new ArrayList<>();
        _edges = new ArrayList<>();
        _edgeIdCounter = 0;
        
        File f = new File(filename);
        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                ParseLine(scan.nextLine());
            }
            scan.close();
            BuildDatabase();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void PrintGraphlet(int id) {
        
    }
    
    private void ParseLine(String line) {
        String[] values = line.split(" ");
        switch (values[0]) {
            case "v":
                _nodes.add(new Node(Integer.parseInt(values[1]), values[2]));
                break;
            case "e":
                _edges.add(new Edge(_edgeIdCounter, Integer.parseInt(values[1]), Integer.parseInt(values[2]), values[3]));
                _edgeIdCounter++;
                break;
        }
    }
    
    private void BuildDatabase() {
        CleanTables();
        CreateTables();
        InsertNodes();
        InsertEdges();
        InsertBoundaries();
    }
    
    private void CreateTables() {
        String nodeTable = "CREATE table APP.node (" +
                "vertexId INTEGER NOT NULL " +
                "PRIMARY KEY GENERATED ALWAYS AS IDENTITY " +
                "(START WITH 0, INCREMENT BY 1), " +
                "label VARCHAR(255))";
        String edgeTable = "CREATE table APP.edge (" +
                "edgeId INTEGER NOT NULL " +
                "PRIMARY KEY GENERATED ALWAYS AS IDENTITY " +
                "(START WITH 0, INCREMENT BY 1), " +
                "firstNode INTEGER NOT NULL, " +
                "secondNode INTEGER NOT NULL, " +
                "label VARCHAR(255))";
        String boundaryTable = "CREATE table APP.boundary (" +
                "vertexId INTEGER NOT NULL, " +
                "edgeId INTEGER NOT NULL)";
        
        try {
            Statement s = ItsConnection.createStatement();
            s.execute(nodeTable);
            s.execute(edgeTable);
            s.execute(boundaryTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void InsertNodes() {
        try {
            Statement st = ItsConnection.createStatement();
            String insert = "INSERT INTO APP.node (label) VALUES ('%s')";
            for (Node n : _nodes) {
                st.execute(String.format(insert, n.ItsLabel));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void InsertEdges() {
        try {
            Statement st = ItsConnection.createStatement();
            String insert = "INSERT INTO APP.edge (firstNode, secondNode, label) VALUES (%s , %s, '%s')";
            for (Edge e : _edges) {
                st.execute(String.format(insert, e.ItsFirstNode, e.ItsSecondNode, e.ItsLabel));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void InsertBoundaries() {
        try {
            Statement st = ItsConnection.createStatement();
            String insert = "INSERT INTO APP.boundary VALUES (%s , %s)";
            List<Integer> neighbors;
            for (Node n : _nodes) {
                neighbors = new ArrayList<>();
                for (Edge e : _edges) {
                    if (n.ItsVertexId == e.ItsFirstNode) {
                        neighbors.add(e.ItsSecondNode);
                    } else if (n.ItsVertexId == e.ItsSecondNode) {
                        neighbors.add(e.ItsFirstNode);
                    }
                }
                
                for (Edge e : _edges) {
                    if (neighbors.contains(e.ItsFirstNode) && neighbors.contains(e.ItsSecondNode)) {
                        st.execute(String.format(insert, n.ItsVertexId, e.ItsEdgeId));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void CleanTables() {
        try {
            Statement st = ItsConnection.createStatement();
            String drop = "DROP TABLE %s";
            st.execute(String.format(drop, "APP.node"));
            st.execute(String.format(drop, "APP.edge"));
            st.execute(String.format(drop, "APP.boundary"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
