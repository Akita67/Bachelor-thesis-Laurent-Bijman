import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Graph graph = new Graph(); // For simulations
        Random rand = new Random();

        // Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);v4.charging_station=true;
        Vertex v5 = new Vertex(5);//v5.charging_station=true;v5.fast_charging=true;
        Vertex v6 = new Vertex(6);


        // Add vertices
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);

        // Add edges
        graph.addEdge(v1, v2, 1); //rand.nextInt(1,10)
        graph.addEdge(v1, v3, 3);
        graph.addEdge(v2, v4, 1);
        graph.addEdge(v3, v5, 3);
        graph.addEdge(v4, v6,1);
        graph.addEdge(v5, v6,1);

        // Print
        System.out.println("Graph representation:");
        graph.printGraph();


        List<Vertex> shortestPath = graph.shortestPath(v1, v6);
        Agent a1 = new Agent(1);
        a1.travel(graph, shortestPath);

        List<Vertex> shortestPath2 = graph.shortestPath(v1, v6);
        Agent a2 = new Agent(2);
        a2.travel(graph, shortestPath2);

        List<Vertex> shortestPath3 = graph.shortestPath(v1, v6);
        Agent a3 = new Agent(3);
        a3.travel(graph, shortestPath3);

        List<Vertex> shortestPath4 = graph.shortestPath(v1, v6);
        Agent a4 = new Agent(4);
        a4.travel(graph, shortestPath4);


    }
}