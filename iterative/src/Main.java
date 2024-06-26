import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        boolean stop = false;

        Graph graph = new Graph();

// Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);v3.charging_station=true;v3.fast_charging=true;
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);v7.charging_station=true;
        Vertex v8 = new Vertex(8);
        Vertex v9 = new Vertex(9);v9.charging_station=true;
        Vertex v10 = new Vertex(10);
        Vertex v11 = new Vertex(11);
        Vertex v12 = new Vertex(12);
        Vertex v13 = new Vertex(13);v13.charging_station=true;v13.fast_charging=true;
        Vertex v14 = new Vertex(14);
        Vertex v15 = new Vertex(15);

// Add vertices to the graph
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);
        graph.addVertex(v8);
        graph.addVertex(v9);
        graph.addVertex(v10);
        graph.addVertex(v11);
        graph.addVertex(v12);
        graph.addVertex(v13);
        graph.addVertex(v14);
        graph.addVertex(v15);


// Add edges to the graph
// Syntax: addEdge(Vertex source, Vertex destination, int weight)
        graph.addEdge(v1, v2, 50.0);
        graph.addEdge(v2, v3, 50.0);
        graph.addEdge(v3, v4, 50.0);
        graph.addEdge(v4, v5, 50.0);

        graph.addEdge(v6, v7, 50.0);
        graph.addEdge(v7, v8, 50.0);
        graph.addEdge(v8, v9, 50.0);
        graph.addEdge(v9, v10, 50.0);

        graph.addEdge(v11, v12, 50.0);
        graph.addEdge(v12, v13, 50.0);
        graph.addEdge(v13, v14, 50.0);
        graph.addEdge(v14, v15, 50.0);

        graph.addEdge(v1, v6, 50.0);
        graph.addEdge(v6, v11, 50.0);
        graph.addEdge(v2, v7, 50.0);
        graph.addEdge(v7, v12, 50.0);
        graph.addEdge(v3, v8, 50.0);
        graph.addEdge(v8, v13, 50.0);
        graph.addEdge(v4, v9, 50.0);
        graph.addEdge(v9, v14, 50.0);
        graph.addEdge(v5, v10, 50.0);
        graph.addEdge(v10, v15, 50.0);

        int temp=20;
        List<Agent> agents = new ArrayList<>();
        for (int i = 1; i < temp+1; i++) {
            agents.add(new Agent(i, v5, v6));
        }
        for (int i = temp+1; i < 2*temp+1; i++) {
            agents.add(new Agent(i, v4, v12 ));
        }
        for (int i = 2*temp+1; i < 3*temp+1; i++) {
            agents.add(new Agent(i, v1, v8));
        }
        for (int i = 3*temp+1; i < 4*temp+1; i++) {
            agents.add(new Agent(i, v1, v15));
        }
        for (int i = 4*temp+1; i < 5*temp+1; i++) {
            agents.add(new Agent(i, v11, v4));
        }
        for (int i = 5*temp+1; i < 6*temp+1; i++) {
            agents.add(new Agent(i, v14, v6));
        }
        for (int i = 6*temp+1; i < 7*temp+1; i++) {
            agents.add(new Agent(i, v12, v1));
        }
        for (int i = 7*temp+1; i < 8*temp+1; i++) {
            agents.add(new Agent(i, v8, v1));
        }
        for (int i = 8*temp+1; i < 9*temp+1; i++) {
            agents.add(new Agent(i, v8, v10));
        }
        for (int i = 9*temp+1; i < 10*temp+1; i++) {
            agents.add(new Agent(i, v8, v12));
        }




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
            System.out.println(agent.current_distance);
            if(agent.current_distance>max_time){
                max_time = agent.current_distance;
            }
        }
        System.out.println("The makespan of this problem is " + max_time);
        for (Agent a: agents) {
            System.out.print(a.getCharging_station().getId() + " ");
        }
    }
}