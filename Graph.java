import java.util.*;

class Graph {
    protected Map<Vertex, List<Edge>> adjacencyList;

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
    // Greedy Algorithm to find the shortest path
    public List<Vertex> shortestPath(Vertex start, Vertex end) {
        Map<Vertex, Integer> time = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(time::get));

        // Initialize distances
        for (Vertex vertex : adjacencyList.keySet()) {
            if (vertex == start) {
                time.put(vertex, 0);
                priorityQueue.add(vertex);
            } else {
                time.put(vertex, Integer.MAX_VALUE);
            }
            previous.put(vertex, null);
        }

        while (!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();
            if (current == end) {
                break; // Found the shortest path to the end vertex
            }
            List<Edge> edges = adjacencyList.get(current);
            for (Edge neighbor : edges) {
                int newDistance = time.get(current) + neighbor.cost;

                if (neighbor.destination.charging_station) {
                    // Add charging time if it's a charging spot
                    if(neighbor.destination.fast_charging)
                        newDistance += newDistance/2;
                    else
                        newDistance += newDistance;
                }

                if (newDistance < time.get(neighbor.destination)) {
                    time.put(neighbor.destination, newDistance);
                    previous.put(neighbor.destination, current);
                    priorityQueue.add(neighbor.destination);
                }
            }
        }

        // Reconstruct the path
        List<Vertex> path = new ArrayList<>();
        Vertex current = end;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);
        return path;
    }
    public Map<Vertex, List<Edge>> getAdjacencyList(){
        return adjacencyList;
    }

}