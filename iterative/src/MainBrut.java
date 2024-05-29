import java.text.DecimalFormat;
import java.util.*;

public class MainBrut {

    private static final long MAX_TIME_MILLIS = 1000; // Maximum time in milliseconds (10 minutes)
    private static final long startTime = System.currentTimeMillis();
    private static Set<List<Vertex>> uniqueAssignments = new HashSet<>();
    private static int counting = 0;
    public static void main(String[] args) {

        Graph graph = new Graph();

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



        List<Agent> agents = new ArrayList<>();
        int temp = 1;
        for (int i = 1; i < temp+1; i++) {
            agents.add(new Agent(i, v1, v10));
        }
        for (int i = temp+1; i < 2*temp+1; i++) {
            agents.add(new Agent(i, v11, v5));

        }
        for (int i = 2*temp+1; i < 3*temp+1; i++) {
            agents.add(new Agent(i, v6, v15));
        }


        List<Vertex> list_chargingS = graph.getChargingStations();

        List<List<Vertex>> result = new ArrayList<>();
        List<Vertex> currentAssignment = new ArrayList<>();

        generateAssignments2(agents, list_chargingS, 0, currentAssignment, result);
        System.out.println("number of assignments generated " + counting);
        System.out.println("number of assignments after pruning " + result.size());

        double minMakeSpan = Double.MAX_VALUE;
        List<Vertex> minList = new ArrayList<>();
        double maxMakeSpan = Double.MIN_VALUE;
        List<Vertex> maxList = new ArrayList<>();

        /*
        result.clear();
        int alloc = 34;
        List<Vertex> specific = new ArrayList<>();
        for (int i = 0; i < alloc; i++) {
            specific.add(v4);
        }
        for (int i = alloc; i < 100; i++) {
            specific.add(v5);
        }
        result.add(specific);
         */



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

    public static void generateAssignments2(List<Agent> agents, List<Vertex> chargingStations, int agentIndex, List<Vertex> currentAssignment, List<List<Vertex>> result) {
        if (System.currentTimeMillis() - startTime >= MAX_TIME_MILLIS) {
            System.out.println("Time has passed");
            // If time exceeds the maximum allowed time, return what you have
            return;
        }
        if (agentIndex == agents.size()) {
            // Convert current assignment to a List and check if it's unique
            List<Vertex> assignment = new ArrayList<>(currentAssignment);
            assignment.sort(new VertexComparator());
            counting++;
            if (uniqueAssignments.add(assignment)) {
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

    static class VertexComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex v1, Vertex v2) {
            // Implement comparison logic based on Vertex properties
            return v1.getId() - v2.getId(); // Example comparison by ID
        }
    }
}