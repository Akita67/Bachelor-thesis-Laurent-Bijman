import java.util.*;

public class Graph {
    private int V; //number of vertices
    private LinkedList<Edge> adjList[];

    class Edge {
        int dest;
        int weight;

        Edge(int dest, int weight) {
            this.dest = dest;
            this.weight = weight;
        }
    }


    Graph(int v){
        V = v;
        adjList = new LinkedList[v];
        for (int i = 0; i < v; i++)
            adjList[i] = new LinkedList();

    }

    void addEdge(int src, int dest, int weight){ //add an edge
        if(src == dest)
            return; // Exit when we have inner loop
        for (Edge edge : adjList[src]) {
            if (edge.dest == dest) {
                System.out.println("Edge between " + src + " and " + dest + " already exists.");
                return; // Exit the method if the edge already exists
            }
        }
        adjList[src].add(new Edge(dest, weight));
        adjList[dest].add(new Edge(src, weight)); //both ways
    }

    void printGraph() {
        for (int i = 0; i < V; i++) {
            System.out.println("adjacency list " + i);
            for(Edge value : adjList[i]){
                System.out.println("dest: " + value.dest + " weight: " + value.weight);
            }
            System.out.println();
        }
    }

}
