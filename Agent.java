import java.util.*;
public class Agent {
    protected int id;
    protected int charging_time = 0; // the time the car needs to charge
    protected int total_time = 0; // In the end the total time the car was active

    public Agent(int id) {
        this.id = id;
    }

    // Method to travel along the path
    // TODO take into account the charging time to make better predictions
    public void travel(Graph graph, List<Vertex> path) {
        Map<Vertex, List<Edge>> adjacencyList = graph.getAdjacencyList();
        System.out.println("Agent " + id + " is traveling:");
        total_time = 0;
        int charging_t = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex currentVertex = path.get(i);
            Vertex nextVertex = path.get(i + 1);
            List<Edge> edges = adjacencyList.get(currentVertex);
            for (Edge edge : edges) {
                if (edge.destination == nextVertex) {
                    total_time += edge.cost;
                    if (edge.destination.charging_station) {

                        if(edge.destination.fast_charging) { charging_t = total_time / 2;}
                        else{ charging_t = total_time;}
                        edge.destination.queue.add(charging_t);

                        int waiting_t = 0;
                        int all_waiting_t = 0;
                        for (int j = 0; j < edge.destination.queue.size()-1; j++) {
                            all_waiting_t += edge.destination.queue.get(j);
                        }
                        waiting_t = Math.max(0,edge.destination.time_first + all_waiting_t - total_time);


                        total_time += charging_t + waiting_t;

                    }
                    break;
                }
            }
            System.out.println("Step " + (i + 1) + ": Move from " + currentVertex.id + " to " + nextVertex.id + ". Time: " + total_time);
        }
    }

}
