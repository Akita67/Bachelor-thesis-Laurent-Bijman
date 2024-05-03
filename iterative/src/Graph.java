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
        edges.add(new Edge(destination, source, weight));
    }

    public List<Vertex> shortestPath(Vertex start, Vertex end, Agent agent, List<Agent> agents) {
        Map<Vertex, Double> time = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(time::get));
        boolean flag = false;
        List<Vertex> temp = new ArrayList<>();

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
            Vertex current = priorityQueue.poll(); // takes the smallest
            double newDistance = time.get(current);

            if(current.charging_station && !agent.did_charge){
                flag = true;
                GameManager gameManager = new GameManager();
                double waiting = gameManager.getTotalWaitingTime(agents,newDistance,current); //waiting is always zero need to check
                double charging_t = gameManager.getChargingTime(agent,current);
                newDistance += waiting + charging_t;
                temp = shortestPath(current,end,agent,agents);
                break;
            }
            if (current == end && !agent.did_charge) {
                newDistance = Double.MAX_VALUE;
            }
            if(current == end && agent.did_charge){
                break;
            }

            List<Edge> edges = getOutgoingEdges(current);
            for (Edge neighbor : edges) {
                // allows to not go back to already previously seen node (if the weights are higher than before)

                double edgeWeight = newDistance + neighbor.getWeight();
                if(neighbor.destination==end && !agent.did_charge){
                    edgeWeight = Double.MAX_VALUE;
                }
                if (edgeWeight < time.get(neighbor.destination) ) {
                    time.put(neighbor.destination, edgeWeight);
                    previous.put(neighbor.destination, current);
                    priorityQueue.add(neighbor.destination);
                }
            }
        }
        // Reconstruct the path
        List<Vertex> path = new ArrayList<>();
        Vertex current = end;
        if(temp.size()>0){
            current = temp.get(0);
        }
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);
        if(flag){
            agent.did_charge=false;
            agent.charging_station=null;
            agent.charging_time=0;
        }

        if(temp.size()>0){
            path.remove(path.size()-1);
        }
        for(Vertex t : temp){
            path.add(t);
        }

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
    public List<Vertex> Brut_shortestPath(Vertex start, Vertex end, Agent agent, List<Agent> agents) {
        Map<Vertex, Double> time = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(time::get));
        boolean flag = false;
        List<Vertex> temp = new ArrayList<>();

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
            Vertex current = priorityQueue.poll(); // takes the smallest
            double newDistance = time.get(current);

            if(current.equals(agent.charging_station) && !agent.did_charge){
                flag = true;
                GameManager gameManager = new GameManager();
                double waiting = gameManager.getTotalWaitingTime(agents,newDistance,current); //waiting is always zero need to check
                double charging_t = gameManager.getChargingTime(agent,current);
                newDistance += waiting + charging_t;
                temp = Brut_shortestPath(current,end,agent,agents);
                break;
            }
            if (current == end && !agent.did_charge) {
                newDistance = Double.MAX_VALUE;
            }
            if(current == end && agent.did_charge){
                break;
            }

            List<Edge> edges = getOutgoingEdges(current);
            for (Edge neighbor : edges) {
                // allows to not go back to already previously seen node (if the weights are higher than before)

                double edgeWeight = newDistance + neighbor.getWeight();
                if(neighbor.destination==end && !agent.did_charge){
                    edgeWeight = Double.MAX_VALUE;
                }
                if (edgeWeight < time.get(neighbor.destination) ) {
                    time.put(neighbor.destination, edgeWeight);
                    previous.put(neighbor.destination, current);
                    priorityQueue.add(neighbor.destination);
                }
            }
        }
        // Reconstruct the path
        List<Vertex> path = new ArrayList<>();
        Vertex current = end;
        if(temp.size()>0){
            current = temp.get(0);
        }
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);
        if(flag){
            agent.did_charge=false;
            agent.charging_time=0;
        }

        if(temp.size()>0){
            path.remove(path.size()-1);
        }
        for(Vertex t : temp){
            path.add(t);
        }

        return path;
    }
    public List<Vertex> getChargingStations(){
        List<Vertex> list = new ArrayList<>();
        for(Vertex v : vertices){
            if(v.charging_station){
                list.add(v);
            }
        }
        return list;
    }
}