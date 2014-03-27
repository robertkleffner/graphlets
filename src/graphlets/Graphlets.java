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
        testg.ItsNeighbors.add(new Node(3, "D"));
        testg.ItsEdges.add(new Edge(01, 0, 1, "01"));
        testg.ItsEdges.add(new Edge(02, 0, 2, "02"));
        testg.ItsEdges.add(new Edge(03, 0, 3, "03"));
        testg.ItsBoundaries.add(new Edge(12, 1, 2, "12"));
        testg.ItsBoundaries.add(new Edge(13, 1, 3, "13"));
        testg.ItsBoundaries.add(new Edge(23, 2, 3, "23"));
        m.GetMatchingGraphlets(testg);
        
        i.Shutdown();
    }
}
