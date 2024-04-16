import java.util.Random;
public class Main {
    public static void main(String[] args) {

        Graph graph = new Graph();
        Random rand = new Random();

        // Create vertices
        Vertex v1 = new Vertex();v1.id = 1;
        Vertex v2 = new Vertex();v2.id = 2;
        Vertex v3 = new Vertex();v3.id = 3;
        Vertex v4 = new Vertex();v4.id = 4;v4.charging_station=true;
        Vertex v5 = new Vertex();v5.id = 5;v5.charging_station=true;v5.fast_charging=true;
        Vertex v6 = new Vertex();v6.id = 5;


        // Add vertices
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);

        // Add edges
        graph.addEdge(v1, v2, rand.nextInt(0,10));
        graph.addEdge(v1, v3, rand.nextInt(0,10));
        graph.addEdge(v2, v4, rand.nextInt(0,10));
        graph.addEdge(v3, v5, rand.nextInt(0,10));
        graph.addEdge(v4, v6, rand.nextInt(0,10));
        graph.addEdge(v5, v6, rand.nextInt(0,10));

        // Print
        System.out.println("Graph representation:");
        graph.printGraph();

        Agent a1 = new Agent();a1.id = 1;
        Agent a2 = new Agent();a2.id = 2;



    }
}