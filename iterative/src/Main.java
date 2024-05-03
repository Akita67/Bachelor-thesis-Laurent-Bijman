import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Graph graph = new Graph();
        boolean stop = false;

        // Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);v2.charging_station=true;
        Vertex v3 = new Vertex(3);v3.charging_station=true;
        Vertex v4 = new Vertex(4);v4.charging_station=true;
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
        graph.addEdge(v1, v2, 5.0);
        graph.addEdge(v2, v3, 5.0);
        graph.addEdge(v3, v4, 5.0);
        graph.addEdge(v4, v5, 5.0);
        graph.addEdge(v5, v6, 5.0);

        Agent agent1 = new Agent(1,v1,v6);
        Agent agent2 = new Agent(2,v1,v6);
        Agent agent3 = new Agent(3,v1,v6);
        List<Agent> agents = new ArrayList<>();
        agents.add(agent1);agents.add(agent2);agents.add(agent3);

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
        double max_time = Double.MIN_VALUE;
        for(Agent agent: agents){
            if(agent.current_distance>max_time){
                max_time = agent.current_distance;
            }
        }
        System.out.println("The makespan of this problem is " + max_time);
    }
}