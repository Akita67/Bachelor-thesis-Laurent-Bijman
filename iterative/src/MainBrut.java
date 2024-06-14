import java.util.*;

public class MainBrut {

    private static final long MAX_TIME_MILLIS = 2000; // Maximum time in milliseconds (10 minutes)
    private static final long startTime = System.currentTimeMillis();
    private static Set<String> uniqueAssignments = new HashSet<String>();
    private static int counting = 0;
    public static void main(String[] args) {

        Graph graph = new Graph();

// Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);v4.charging_station=true;
        Vertex v5 = new Vertex(5);v5.charging_station=true;v5.fast_charging=true;
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);


// Add vertices to the graph
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);


// Add edges to the graph
// Syntax: addEdge(Vertex source, Vertex destination, int weight)
        graph.addEdge(v1, v2, 100.0);
        graph.addEdge(v2, v3, 50.0);
        graph.addEdge(v3, v4, 50.0);
        graph.addEdge(v4, v5, 5.0);

        graph.addEdge(v4, v6, 10.0);
        graph.addEdge(v5, v7, 10.0);



        List<Agent> agents = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            agents.add(new Agent(i, v1, v6));
        }
        for (int i = 101; i < 201; i++) {
            agents.add(new Agent(i, v1, v7));
        }


        List<Vertex> list_chargingS = graph.getChargingStations();

        List<List<Vertex>> result = new ArrayList<>();
        List<Vertex> currentAssignment = new ArrayList<>();

        generateAssignments2(agents, list_chargingS, 0, currentAssignment, result);
        System.out.println("number of assignments generated " + counting);
        System.out.println("number of assignments after pruning " + result.size());

        double minMakeSpan = Double.MAX_VALUE;
        List<Vertex> minList = new ArrayList<>();


        result.clear();

        List<Vertex> specific = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            specific.add(v4);
        }
        for (int i = 0; i < 100; i++) {
            specific.add(v5);
        }
        result.add(specific);




        for (List<Vertex> assignment : result) {
            boolean stop = false;
            for (Agent a : agents){
                a.reset();
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
            if(max_time<minMakeSpan){
                minMakeSpan = max_time;
                minList = assignment;
                System.out.println("The new optimum " + minMakeSpan);
                for (Vertex a:minList) {
                    System.out.print(a.getId() + " ");
                }
                System.out.println();
            }
        }
        System.out.println("The Optimum makespan is " + minMakeSpan);
        for (int i = 0; i < minList.size(); i++) {
            System.out.print(minList.get(i).getId() + " ");
        }
    }

    public static void generateAssignments2(List<Agent> agents, List<Vertex> chargingStations, int agentIndex, List<Vertex> currentAssignment, List<List<Vertex>> result) {
        if (System.currentTimeMillis() - startTime >= MAX_TIME_MILLIS) {
            System.out.println("Time has passed");
            // If time exceeds the maximum allowed time, return what you have
            return;
        }
        if (agentIndex == agents.size()) {
            // Convert current assignment to a unique string representation including start and end positions
            String assignmentStr = getAssignmentString(agents, currentAssignment);
            counting++;
            if (uniqueAssignments.add(assignmentStr)) {
                result.add(new ArrayList<>(currentAssignment));
            }
            return;
        }

        Agent currentAgent = agents.get(agentIndex);
        List<Vertex> shuffledChargingStations = new ArrayList<>(chargingStations);
        Collections.shuffle(shuffledChargingStations); // Shuffle to introduce randomness

        for (Vertex chargingStation : shuffledChargingStations) {
            currentAgent.setCharging_station(chargingStation);
            currentAssignment.add(chargingStation);
            generateAssignments2(agents, chargingStations, agentIndex + 1, currentAssignment, result);
            currentAssignment.remove(currentAssignment.size() - 1);
            currentAgent.setCharging_station(null); // Resetting charging station assignment
        }
    }
    private static String getAssignmentString(List<Agent> agents, List<Vertex> assignment) {
        // Group agents by their start and end positions
        Map<String, List<Vertex>> groupedAssignments = new HashMap<>();

        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            Vertex chargingStation = assignment.get(i);
            String groupKey = agent.getStart_vertex().getId() + "-" + agent.getDestination_vertex().getId();

            groupedAssignments.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(chargingStation);
        }

        // Create a string representation of grouped assignments, sorting within each group
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Vertex>> entry : groupedAssignments.entrySet()) {
            sb.append(entry.getKey()).append(":");
            List<Vertex> stations = entry.getValue();
            stations.sort(Comparator.comparingInt(Vertex::getId));
            for (Vertex station : stations) {
                sb.append(station.getId()).append(",");
            }
            sb.append(";");
        }

        return sb.toString();
    }
}