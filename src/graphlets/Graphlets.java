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
        
        Graph query = new Graph();
        query.ItsVertices.add(new Node(0, "A"));
        query.ItsVertices.add(new Node(1, "A"));
        query.ItsVertices.add(new Node(2, "A"));
        query.ItsEdges.add(new Edge(01, 0, 1, "01"));
        query.ItsEdges.add(new Edge(02, 0, 2, "02"));
        //query.ItsEdges.add(new Edge(03, 0, 3, "03"));
        //query.ItsEdges.add(new Edge(04, 0, 4, "04"));
        //query.ItsEdges.add(new Edge(12, 1, 2, "12"));
        //query.ItsEdges.add(new Edge(14, 1, 4, "14"));
        //query.ItsEdges.add(new Edge(13, 1, 3, "13"));
        //query.ItsEdges.add(new Edge(23, 2, 3, "23"));
        //query.ItsEdges.add(new Edge(34, 3, 4, "34"));
        //query.ItsEdges.add(new Edge(25, 2, 5, "25"));
        //query.ItsEdges.add(new Edge(35, 3, 5, "35"));
        m.GetMatchingGraphs(query, false);
        
        i.Shutdown();
    }
}
