import java.util.ArrayList;
import java.util.List;

public class MainBrut {
    public static void main(String[] args) {

        Graph graph = new Graph();

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


        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent(1, v1, v6));
        agents.add(new Agent(2, v1, v6));
        agents.add(new Agent(3, v1, v6));

        List<Vertex> list_chargingS = graph.getChargingStations();
        List<Vertex> starting_Pos = new ArrayList<>();
        for (Agent agent : agents){
            starting_Pos.add(agent.currentPosition);
        }

        List<List<Vertex>> result = new ArrayList<>();
        List<Vertex> currentAssignment = new ArrayList<>();

        generateAssignments(agents, list_chargingS, 0, currentAssignment, result);

        double minMakeSpan = Double.MAX_VALUE;
        double maxMakeSpan = Double.MIN_VALUE;
        for (List<Vertex> assignment : result) {
            System.out.println(assignment.get(0).getId() + " " + assignment.get(1).getId() + " " +assignment.get(2).getId());
            boolean stop = false;
            for (Agent a : agents){
                a.reset(starting_Pos.get((a.getId())-1));
                a.setCharging_station(assignment.get(a.getId()-1));
            }
            GameManager game = new GameManager();
            while(!stop){
                game.BrutdetermineNextMove(agents, graph);
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
            if(max_time<minMakeSpan){
                minMakeSpan = max_time;
            }
            if(max_time>maxMakeSpan){
                maxMakeSpan = max_time;
            }
        }
        System.out.println("The Optimum makespan is " + minMakeSpan);
        System.out.println("The worst makespan is " + maxMakeSpan);

    }
    private static void generateAssignments(List<Agent> agents, List<Vertex> chargingStations, int agentIndex, List<Vertex> currentAssignment, List<List<Vertex>> result) {
        if (agentIndex == agents.size()) {
            result.add(new ArrayList<>(currentAssignment));
            return;
        }

        Agent currentAgent = agents.get(agentIndex);
        for (Vertex chargingStation : chargingStations) {
            currentAgent.setCharging_station(chargingStation);
            currentAssignment.add(chargingStation);
            generateAssignments(agents, chargingStations, agentIndex + 1, currentAssignment, result);
            currentAssignment.remove(currentAssignment.size() - 1);
            currentAgent.setCharging_station(null); // Resetting charging station assignment
        }
    }
}