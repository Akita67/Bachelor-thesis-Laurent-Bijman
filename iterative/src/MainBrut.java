import java.text.DecimalFormat;
import java.util.*;

public class MainBrut {

    private static final long MAX_TIME_MILLIS = 300000; // Maximum time in milliseconds (5 minutes)
    private static final long startTime = System.currentTimeMillis();
    public static void main(String[] args) {


        Graph graph = new Graph();

// Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);v2.charging_station=true;
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);v4.charging_station=true;
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);
        Vertex v8 = new Vertex(8);
        Vertex v9 = new Vertex(9);
        Vertex v10 = new Vertex(10);
        Vertex v11 = new Vertex(11);
        Vertex v12 = new Vertex(12);v12.charging_station=true;v12.fast_charging=true;
        Vertex v13 = new Vertex(13);
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
        graph.addEdge(v1, v2, 15.0);
        graph.addEdge(v2, v3, 15.0);
        graph.addEdge(v3, v4, 15.0);
        graph.addEdge(v4, v5, 15.0);

        graph.addEdge(v6, v7, 15.0);
        graph.addEdge(v7, v8, 15.0);
        graph.addEdge(v8, v9, 15.0);
        graph.addEdge(v9, v10, 15.0);

        graph.addEdge(v11, v12, 15.0);
        graph.addEdge(v12, v13, 15.0);
        graph.addEdge(v13, v14, 15.0);
        graph.addEdge(v14, v15, 15.0);

        graph.addEdge(v1, v6, 15.0);
        graph.addEdge(v6, v11, 15.0);
        graph.addEdge(v2, v7, 15.0);
        graph.addEdge(v7, v12, 15.0);
        graph.addEdge(v3, v8, 15.0);
        graph.addEdge(v8, v13, 15.0);
        graph.addEdge(v4, v9, 15.0);
        graph.addEdge(v9, v14, 15.0);
        graph.addEdge(v5, v10, 15.0);
        graph.addEdge(v10, v15, 15.0);


        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent(1, v1, v10));
        agents.add(new Agent(2, v1, v10));
        agents.add(new Agent(3, v1, v10));
        agents.add(new Agent(4, v1, v10));
        agents.add(new Agent(5, v1, v10));
        agents.add(new Agent(6, v1, v10));
        agents.add(new Agent(7, v1, v10));
        agents.add(new Agent(8, v1, v10));
        agents.add(new Agent(9, v6, v15));
        agents.add(new Agent(10, v6, v15));
        agents.add(new Agent(11, v6, v15));
        agents.add(new Agent(12, v6, v15));
        agents.add(new Agent(13, v6, v15));
        agents.add(new Agent(14, v6, v15));
        agents.add(new Agent(15, v6, v15));




        List<Vertex> list_chargingS = graph.getChargingStations();
        List<Vertex> starting_Pos = new ArrayList<>();
        for (Agent agent : agents){
            starting_Pos.add(agent.currentPosition);
        }

        List<List<Vertex>> result = new ArrayList<>();
        List<Vertex> currentAssignment = new ArrayList<>();

        generateAssignments2(agents, list_chargingS, 0, currentAssignment, result);
        System.out.println(result.size());
        //pruning(agents,result); //TODO pruning doesn't work accurately at the moment

        double minMakeSpan = Double.MAX_VALUE;
        List<Vertex> minList = new ArrayList<>();
        double maxMakeSpan = Double.MIN_VALUE;
        List<Vertex> maxList = new ArrayList<>();
        /*
        result.clear();
        List<Vertex> specific = new ArrayList<>();
        specific.add(v2);specific.add(v2);specific.add(v2);specific.add(v3);specific.add(v3);specific.add(v3);specific.add(v4);specific.add(v4);specific.add(v4);specific.add(v);
        result.add(specific); */

        for (List<Vertex> assignment : result) {
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
        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println('\n' + "PoA is : " + df.format(maxMakeSpan/minMakeSpan));

    }


    private static void generateAssignments(List<Agent> agents, List<Vertex> chargingStations, int agentIndex, List<Vertex> currentAssignment, List<List<Vertex>> result) {
        if (agentIndex == agents.size()) {
            result.add(new ArrayList<>(currentAssignment));
            return;
        }

        Agent currentAgent = agents.get(agentIndex);
        for (Vertex chargingStation : chargingStations) {
            // Check if there are agents that start at a same position and all end at another similar position, if that is the case then don't need to generate that possibility
            currentAgent.setCharging_station(chargingStation);
            currentAssignment.add(chargingStation);
            generateAssignments(agents, chargingStations, agentIndex + 1, currentAssignment, result);
            currentAssignment.remove(currentAssignment.size() - 1);
            currentAgent.setCharging_station(null); // Resetting charging station assignment
        }
    }

    private static void generateAssignments2(List<Agent> agents, List<Vertex> chargingStations, int agentIndex, List<Vertex> currentAssignment, List<List<Vertex>> result) {
        if (System.currentTimeMillis() - startTime >= MAX_TIME_MILLIS) {
            System.out.println("Time has passed");
            // If time exceeds the maximum allowed time, return what you have
            return;
        }
        if (agentIndex == agents.size()) {
            // Check if the current assignment is unique before adding it to the result
            if (!isAssignmentUnique(result, currentAssignment)) {
                return; // Skip if not unique
            }
            result.add(new ArrayList<>(currentAssignment));
            return;
        }

        Agent currentAgent = agents.get(agentIndex);
        for (Vertex chargingStation : chargingStations) {
            currentAgent.setCharging_station(chargingStation);
            currentAssignment.add(chargingStation);
            generateAssignments2(agents, chargingStations, agentIndex + 1, currentAssignment, result);
            currentAssignment.remove(currentAssignment.size() - 1);
            currentAgent.setCharging_station(null); // Resetting charging station assignment
        }
    }

    // Check if the current assignment is unique compared to existing assignments
    private static boolean isAssignmentUnique(List<List<Vertex>> assignments, List<Vertex> currentAssignment) {
        for (List<Vertex> assignment : assignments) {
            if (isSameAssignment(assignment, currentAssignment)) {
                return false; // Not unique
            }
        }
        return true; // Unique
    }

    // Check if two assignments are the same (ignoring order)
    private static boolean isSameAssignment(List<Vertex> assignment1, List<Vertex> assignment2) {
        if (assignment1.size() != assignment2.size()) {
            return false;
        }
        List<Vertex> sorted1 = new ArrayList<>(assignment1);
        List<Vertex> sorted2 = new ArrayList<>(assignment2);
        sorted1.sort(new VertexComparator());
        sorted2.sort(new VertexComparator());
        return sorted1.equals(sorted2);
    }

    // Custom comparator for sorting vertices
    private static class VertexComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex v1, Vertex v2) {
            // Implement comparison logic here, for example:
            return Integer.compare(v1.getId(), v2.getId());
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