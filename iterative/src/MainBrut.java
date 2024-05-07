import java.util.*;

public class MainBrut {
    public static void main(String[] args) {

        Graph graph = new Graph();

        // Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);v3.charging_station=true;v3.fast_charging=true;
        Vertex v4 = new Vertex(4);v4.charging_station=true;
        Vertex v5 = new Vertex(5);v5.charging_station=true;
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);
        Vertex v8 = new Vertex(8);


        // Add vertices to the graph
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);

        graph.addVertex(v8);


        // Add edges to the graph
        // Syntax: addEdge(Vertex source, Vertex destination, int weight)
        graph.addEdge(v1, v2, 1.0);
        graph.addEdge(v2, v3, 50.0);
        graph.addEdge(v3, v4, 10.0);
        graph.addEdge(v4, v5, 5.0);

        graph.addEdge(v4, v6, 10.0);
        graph.addEdge(v5, v7, 10.0);

        graph.addEdge(v4,v8,20);



        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent(1, v2, v7));
        agents.add(new Agent(2, v1, v6));
        agents.add(new Agent(3, v2, v7));
        agents.add(new Agent(4, v1, v6));
        agents.add(new Agent(5, v2, v7));
        agents.add(new Agent(6, v1, v6));
        agents.add(new Agent(7, v2, v7));
        agents.add(new Agent(8, v1, v6));
        agents.add(new Agent(9, v2, v7));
        agents.add(new Agent(10, v1, v6));
        agents.add(new Agent(11, v2, v7));
        agents.add(new Agent(12, v1, v6));
        agents.add(new Agent(13, v2, v7));
        agents.add(new Agent(14, v1, v6));
        //agents.add(new Agent(15, v2, v7));
        //agents.add(new Agent(16, v1, v6));
        //agents.add(new Agent(17, v2, v7));
        //agents.add(new Agent(18, v1, v6));
        //agents.add(new Agent(19, v2, v7));
        //agents.add(new Agent(20, v1, v6));

        List<Vertex> list_chargingS = graph.getChargingStations();
        List<Vertex> starting_Pos = new ArrayList<>();
        for (Agent agent : agents){
            starting_Pos.add(agent.currentPosition);
        }

        List<List<Vertex>> result = new ArrayList<>();
        List<Vertex> currentAssignment = new ArrayList<>();

        generateAssignments(agents, list_chargingS, 0, currentAssignment, result);
        pruning(agents,result);

        double minMakeSpan = Double.MAX_VALUE;
        List<Vertex> minList = new ArrayList<>();
        double maxMakeSpan = Double.MIN_VALUE;
        List<Vertex> maxList = new ArrayList<>();
        for (List<Vertex> assignment : result) { //TODO prune symmetric agents
            //System.out.println(assignment.get(0).getId() + " " + assignment.get(1).getId());
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
            //System.out.println("The makespan of this problem is " + max_time);
            if(max_time<minMakeSpan){
                minMakeSpan = max_time;
                minList = assignment;
                System.out.println("The new optimum " + minMakeSpan);
            }
            if(max_time>maxMakeSpan){
                maxMakeSpan = max_time;
                maxList = assignment;
                System.out.println("The new worst " + maxMakeSpan);
            }
        }
        System.out.println("The Optimum makespan is " + minMakeSpan);
        for (int i = 0; i < minList.size(); i++) {
            System.out.print(minList.get(i).getId() + " ");
        }
        System.out.println('\n' + "The worst makespan is " + maxMakeSpan);
        for (int i = 0; i < minList.size(); i++) {
            System.out.print(maxList.get(i).getId() + " ");
        }

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
    private static void pruning(List<Agent> agents, List<List<Vertex>> result) {
        // Create a set to store unique combinations of charging stations
        Set<List<Vertex>> uniqueStations = new HashSet<>();

        // Iterate over each combination of charging stations for agents
        for (List<Vertex> stations : result) {
            // Sort the list of charging stations for canonical representation
            Collections.sort(stations, Comparator.comparingInt(v -> v.id));

            // Check if this combination is unique
            if (!uniqueStations.contains(stations)) {
                // If it's unique, add it to the set of unique combinations
                uniqueStations.add(stations);
            }
        }

        // Replace the global list with the set of unique combinations
        result.clear();
        result.addAll(uniqueStations);
    }
}