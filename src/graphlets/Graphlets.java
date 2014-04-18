package graphlets;

public class Graphlets {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Specify a file name to load!");
        }
        
        Importer i = new Importer();
        i.Connect();
        i.ImportFile(args[0]);
        
        Matcher m = new Matcher(i.ItsConnection);
        Graphlet testg = new Graphlet();
        testg.ItsCenter = new Node(0, "A");
        testg.ItsNeighbors.add(new Node(1, "B"));
        testg.ItsNeighbors.add(new Node(2, "C"));
        testg.ItsEdges.add(new Edge(01, 0, 1, "01"));
        testg.ItsEdges.add(new Edge(02, 0, 2, "02"));
        //m.GetMatchingGraphlets(testg, false);
        
        Graph query = new Graph();
        query.ItsVertices.add(new Node(0, "A"));
        query.ItsVertices.add(new Node(1, "B"));
        query.ItsVertices.add(new Node(2, "C"));
        query.ItsVertices.add(new Node(3, "D"));
        query.ItsVertices.add(new Node(4, "E"));
        query.ItsEdges.add(new Edge(01, 0, 1, "01"));
        query.ItsEdges.add(new Edge(12, 1, 2, "12"));
        query.ItsEdges.add(new Edge(03, 0, 3, "03"));
        query.ItsEdges.add(new Edge(04, 0, 4, "04"));
        query.ItsEdges.add(new Edge(34, 3, 4, "34"));
        m.GetMatchingGraphs(query, false);
        
        i.Shutdown();
    }
}
