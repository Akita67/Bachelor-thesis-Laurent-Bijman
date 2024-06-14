import java.util.*;

class Graph {
    protected Map<Vertex, List<Edge>> adjacencyList;
    protected final double max_range_time = 50;

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
    // Greedy Algorithm to find the shortest path works when they all start at the same position
    public List<Vertex> shortestPath(Vertex start, Vertex end) {
        Map<Vertex, Double> time = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(time::get));
        double charging_t = 0;

        for (Vertex vertex : adjacencyList.keySet()) {
            if (vertex == start) {
                time.put(vertex, 0.0);
                priorityQueue.add(vertex);
            } else {
                time.put(vertex, Double.MAX_VALUE);
            }
            previous.put(vertex, null);
            vertex.set_EV_has_charged(false);
        }
        while (!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();
            if (current == end) {
                break; // Found the shortest path to the end vertex
            }
            List<Edge> edges = adjacencyList.get(current);
            for (Edge neighbor : edges) {
                double newDistance = time.get(current) + neighbor.cost;
                if (neighbor.destination.charging_station && !current.EV_has_charged) {
                    neighbor.destination.set_EV_has_charged(true);
                    if(neighbor.destination.time_first==0){
                        neighbor.destination.time_first = newDistance;
                    }

                    System.out.println(newDistance);
                    if(neighbor.destination.fast_charging) { charging_t = (newDistance/max_range_time)*30;} // Need to adapt this to actual charging
                    else{ charging_t = (newDistance/max_range_time)*60;} // Need to adapt it (we can assume it's linear)
                    neighbor.destination.queue.add(charging_t);


                    double waiting_t = 0;
                    double all_waiting_t = 0;
                    for (int i = 0; i < neighbor.destination.queue.size()-1; i++) {
                        all_waiting_t += neighbor.destination.queue.get(i);
                    }
                    waiting_t = Math.max(0,neighbor.destination.time_first + all_waiting_t - newDistance);


                    newDistance += charging_t + waiting_t;

                    neighbor.destination.queue.remove(neighbor.destination.queue.size()-1);

                }
                if(current.EV_has_charged){ // The EV has already been charged so don't need to charge it again
                    neighbor.destination.set_EV_has_charged(true);
                }

                if(neighbor.destination == end && !neighbor.destination.EV_has_charged){ // Don't want to allow finishing without charging
                    newDistance = Double.MAX_VALUE;}

                // allows to not go back to already previously seen node (if the weights are higher than before)
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