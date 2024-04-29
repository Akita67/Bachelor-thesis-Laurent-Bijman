import java.util.List;
import java.util.Map;

class GameManager {
    // Method to determine which agent moves first
    public Agent determineNextMove(List<Agent> agents, Graph graph) {
        Agent nextMoveAgent = null;
        double minTotalCost = Double.MAX_VALUE;
        List<Vertex> minpath = null;

        for (Agent agent : agents) {
            List<Vertex> path = dijkstra(graph, agent.currentPosition, agent.destination);
            double nextMoveCost = getNextMoveCost(path, graph);
            double totalCost = agent.current_distance + nextMoveCost;

            if (totalCost < minTotalCost) {
                minTotalCost = totalCost;
                nextMoveAgent = agent;
                minpath = path;
            }
        }
        nextMoveAgent.current_distance = minTotalCost;
        nextMoveAgent.currentPosition = minpath.get(1);
        return nextMoveAgent;
    }

    // Method to calculate the distance between two vertices
    private List<Vertex> dijkstra(Graph graph, Vertex source, Vertex destination){


        return graph.shortestPath(source, destination);
    }
    private double getNextMoveCost(List<Vertex> path, Graph graph) {

        return graph.getEdge(path.get(0),path.get(1)).weight;
    }
}
