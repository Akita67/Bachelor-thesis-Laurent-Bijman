import java.text.DecimalFormat;
import java.util.*;

public class MainBrut {

    private static final long MAX_TIME_MILLIS = 10000; // Maximum time in milliseconds (10 minutes)
    private static final long startTime = System.currentTimeMillis();
    private static Set<List<Vertex>> uniqueAssignments = new HashSet<>();
    private static int counting = 0;
    public static void main(String[] args) {

        Graph graph = new Graph();

// Create vertices representing major cities in the Netherlands
        Vertex amsterdam = new Vertex(1);
        Vertex rotterdam = new Vertex(2);
        Vertex theHague = new Vertex(3);
        Vertex utrecht = new Vertex(4);
        Vertex eindhoven = new Vertex(5);
        Vertex tilburg = new Vertex(6);
        Vertex groningen = new Vertex(7);
        Vertex almere = new Vertex(8);
        Vertex breda = new Vertex(9);
        Vertex nijmegen = new Vertex(10);
        Vertex enschede = new Vertex(11);
        Vertex haarlem = new Vertex(12);
        Vertex arnhem = new Vertex(13);
        Vertex amersfoort = new Vertex(14);
        Vertex maastricht = new Vertex(15);
        Vertex middelburg = new Vertex(16);
        Vertex zwolle = new Vertex(17);
        Vertex helmond = new Vertex(18);
        Vertex ede = new Vertex(19);


// Add vertices to the graph
        graph.addVertex(amsterdam);
        graph.addVertex(rotterdam);rotterdam.charging_station=true;
        graph.addVertex(theHague);theHague.charging_station=true;theHague.fast_charging=true;
        graph.addVertex(utrecht);
        graph.addVertex(eindhoven);eindhoven.charging_station=true;
        graph.addVertex(tilburg);
        graph.addVertex(groningen);
        graph.addVertex(almere);
        graph.addVertex(breda);
        graph.addVertex(nijmegen);
        graph.addVertex(enschede);
        graph.addVertex(haarlem);
        graph.addVertex(arnhem);arnhem.charging_station=true;arnhem.fast_charging=true;
        graph.addVertex(amersfoort);
        graph.addVertex(maastricht);
        graph.addVertex(middelburg);
        graph.addVertex(zwolle);
        graph.addVertex(helmond);
        graph.addVertex(ede);ede.charging_station=true;


// Add edges representing roads between cities (approximate distances in km)
        graph.addEdge(maastricht,eindhoven,70);
        graph.addEdge(maastricht,helmond,69);
        graph.addEdge(eindhoven,breda,50);
        graph.addEdge(eindhoven,tilburg,35);
        graph.addEdge(eindhoven,helmond,20);
        graph.addEdge(helmond,ede,80);
        graph.addEdge(helmond,nijmegen,60);
        graph.addEdge(helmond,tilburg,50);
        graph.addEdge(breda,tilburg,30);
        graph.addEdge(breda,middelburg,68);
        graph.addEdge(breda,rotterdam,57);
        graph.addEdge(rotterdam,middelburg,90);
        graph.addEdge(rotterdam,theHague,27);
        graph.addEdge(rotterdam,utrecht,62);
        graph.addEdge(middelburg,theHague,106);
        graph.addEdge(utrecht,theHague,69);
        graph.addEdge(utrecht,amsterdam,48);
        graph.addEdge(utrecht,amersfoort,28);
        graph.addEdge(utrecht,ede,40);
        graph.addEdge(haarlem,theHague,60);
        graph.addEdge(haarlem,amsterdam,30);
        graph.addEdge(almere,amsterdam,32);
        graph.addEdge(almere,zwolle,60);
        graph.addEdge(almere,amersfoort,40);
        graph.addEdge(nijmegen,arnhem,23);
        graph.addEdge(nijmegen,ede,43);
        graph.addEdge(arnhem,enschede,78);
        graph.addEdge(arnhem,ede,22);
        graph.addEdge(amersfoort,arnhem,52);
        graph.addEdge(amersfoort,zwolle,60);
        graph.addEdge(zwolle,groningen,72);
        graph.addEdge(zwolle,arnhem,53);


// Agents need to be placed in groups otherwise the pruning will not work accurately
        List<Agent> agents = new ArrayList<>();

        int temp = 40;
        for (int i = 1; i < temp+1; i++) {
            agents.add(new Agent(i, groningen, utrecht));
        }
        for (int i = temp+1; i < 2*temp+1; i++) {
            agents.add(new Agent(i, maastricht, utrecht));
        }
        for (int i = 2*temp+1; i < 3*temp+1; i++) {
            agents.add(new Agent(i, breda, utrecht));
        }
        for (int i = 3*temp+1; i < 4*temp+1; i++) {
            agents.add(new Agent(i, haarlem, utrecht));
        }
        for (int i = 4*temp+1; i < 5*temp+1; i++) {
            agents.add(new Agent(i, enschede, utrecht));
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
        List<Vertex> specific = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            specific.add(arnhem);
        }
        for (int i = 20; i < 40; i++) {
            specific.add(ede);
        }
        for (int i = 40; i < 60; i++) {
            specific.add(rotterdam);
        }
        for (int i = 60; i < 80; i++) {
            specific.add(ede);
        }
        for (int i = 80; i < 100; i++) {
            specific.add(ede);
        }
        result.add(specific);*/



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