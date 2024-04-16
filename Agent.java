import java.util.*;
public class Agent {
    protected int id;
    protected int charging_time = 0; // the time the car needs to charge
    protected boolean charged = false; // need to charged at least one time
    protected int total_time = 0; // In the end the total time the car was active

    public Agent(int id) {
        this.id = id;
    }

    // Method to travel along the path
    // TODO take into account the charging time to make better predictions
    public void travel(Graph graph, List<Vertex> path) {
        Map<Vertex, List<Edge>> adjacencyList = graph.getAdjacencyList();
        System.out.println("Agent " + id + " is traveling:");
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex currentVertex = path.get(i);
            Vertex nextVertex = path.get(i + 1);
            List<Edge> edges = adjacencyList.get(currentVertex);
            int edgeCost = 0;
            for (Edge edge : edges) {
                if (edge.destination == nextVertex) {
                    edgeCost = edge.cost;
                    if (currentVertex.charging_station) {
                        if(currentVertex.fast_charging)
                            edgeCost += edgeCost/2;
                        else
                            edgeCost += edgeCost;
                        System.out.println("Charging cost " + edgeCost);
                    }
                    break;
                }
            }
            total_time += edgeCost;
            System.out.println("Step " + (i + 1) + ": Move from " + currentVertex.id + " to " + nextVertex.id + ". Time: " + total_time);
        }
    }

}
