import java.util.*;

class Graph {
    List<Vertex> vertices;
    List<Edge> edges;

    public Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    // Methods to add vertices and edges to the graph
    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addEdge(Vertex source, Vertex destination, double weight) {
        edges.add(new Edge(source, destination, weight));
    }

    public List<Vertex> shortestPath(Vertex start, Vertex end, Agent agent, List<Agent> agents) {
        Map<Vertex, Double> time = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(time::get));

        for (Vertex vertex : vertices) {
            if (vertex == start) {
                time.put(vertex, 0.0);
                priorityQueue.add(vertex);
            } else {
                time.put(vertex, Double.MAX_VALUE);
            }
            previous.put(vertex, null);
        }
        while (!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();
            if (current == end) {
                break; // Found the shortest path to the end vertex
            }
            List<Edge> edges = getOutgoingEdges(current);
            for (Edge neighbor : edges) {
                double newDistance = time.get(current);
                double waiting = 0;
                double charging_t = 0;

                if(current.charging_station && !agent.did_charge){
                    agent.setDid_charge(true);
                    List<Agent> list = null;
                    for(Agent a : agents){
                        if(a.currentPosition.equals(neighbor.destination.charging_station)){
                            list.add(a);
                        }
                    }
                    if(list != null){ // TODO works with one only (don't know if that is a problem)
                        waiting = Math.max((list.get(0).current_distance + list.get(0).charging_time) - agent.current_distance,0);
                    }

                    // TODO check in all the agents if someone is in the charging (same node)
                    // TODO if not do the charging calculation (fast or slow)
                    if(current.fast_charging){charging_t = (newDistance/agent.max_energy_tank)*30;}
                    else{charging_t = (newDistance/agent.max_energy_tank)*60;}
                }

                newDistance = time.get(current) + neighbor.weight + waiting + charging_t;
                if(neighbor.destination == end && !agent.did_charge){ // Don't want to allow finishing without charging
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

    protected List<Edge> getOutgoingEdges(Vertex vertex) {
        List<Edge> outgoingEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.source == vertex) {
                outgoingEdges.add(edge);
            }
        }
        return outgoingEdges;
    }
    protected Edge getEdge(Vertex source, Vertex destination){
        for (Edge edge : edges){
            if(edge.source == source && edge.destination == destination){
                return edge;
            }
        }
        return null;
    }
}