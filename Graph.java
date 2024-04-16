import java.util.*;

class Graph {
    private Map<Vertex, List<Edge>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(Vertex vertex) {
        adjacencyList.put(vertex, new ArrayList<>());
    }

    public void addEdge(Vertex source, Vertex destination, int cost) {
        Edge edge = new Edge();
        edge.source = source;
        edge.destination = destination;
        edge.cost = cost;

        adjacencyList.get(source).add(edge);
        adjacencyList.get(destination).add(edge); // remove this if it's a directed graph
    }

    public void printGraph() {
        for (Map.Entry<Vertex, List<Edge>> entry : adjacencyList.entrySet()) {
            System.out.print(entry.getKey().id + " -> ");
            List<Edge> edges = entry.getValue();
            for (Edge edge : edges) {
                if(edge.destination.id!=entry.getKey().id)
                    System.out.print(edge.destination.id + " ");
                if(edge.source.id!=entry.getKey().id)
                    System.out.print(edge.source.id + " ");
            }
            System.out.println();
        }
    }

}