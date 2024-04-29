import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Graph graph = new Graph();
        boolean stop = false;

        // Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);v5.charging_station=true;
        Vertex v6 = new Vertex(6);


        // Add vertices to the graph
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);


        // Add edges to the graph
        // Syntax: addEdge(Vertex source, Vertex destination, int weight)
        graph.addEdge(v1, v3, 5.0);
        graph.addEdge(v2, v4, 11.0);
        graph.addEdge(v3, v5, 5.0);
        graph.addEdge(v4, v5, 5.0);
        graph.addEdge(v5, v6, 3.0);

        Agent agent1 = new Agent(1,v1,v6);
        Agent agent2 = new Agent(2,v2,v6);
        List<Agent> agents = new ArrayList<>();
        agents.add(agent1);agents.add(agent2);

        GameManager game = new GameManager();
        while(!stop){
            game.determineNextMove(agents, graph);
            for(Agent agent: agents){
                if(agent.currentPosition.equals(agent.destination)){
                    stop = true;
                }
                else{
                    stop = false;
                    break;
                }
            }
        }
    }
}