import java.util.*;

public class NashEquilibrium {

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
        int temp = 20;
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
        List<Vertex> currentAssignment = new ArrayList<>();

        simple_Assign(currentAssignment,agents,list_chargingS);

        int count = 0;
        double value;
        double minMakeSpan = Double.MAX_VALUE;
        boolean flag = false;
        boolean flagremain = false;
        Vertex stockremain;
        List<Vertex> minList = new ArrayList<>();

        while(count < agents.size()){
            stockremain = currentAssignment.get(count);
            for (Vertex charge: list_chargingS) {
                currentAssignment.set(count, charge);
                value = simple_Game(currentAssignment,agents,graph,minMakeSpan);
                //System.out.println(value);
                if(value < minMakeSpan){
                    minMakeSpan=value;
                    minList = new ArrayList<>(currentAssignment);
                    flag=true;
                    flagremain=true;
                    break;
                }
            }
            if (!flagremain){
                currentAssignment.set(count, stockremain);
            }
            count++;
            if(count == agents.size()){
                if (flag){
                    count = 0;
                    flag = false;
                }
            }
        }

        System.out.println("We reach an equilibrium with the makespan of " + minMakeSpan);
        for (int i = 0; i < minList.size(); i++) {
            System.out.print(minList.get(i).getId() + " ");
        }
    }



    public static void simple_Assign(List<Vertex> currentAssignment, List<Agent> agents, List<Vertex> list_chargingS){
        for (int i=0 ; i<agents.size() ; i++) {
            currentAssignment.add(list_chargingS.get(0));
        }
    }
    public static double simple_Game(List<Vertex> assignment, List<Agent> agents, Graph graph, double minMakeSpan){
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
            System.out.println("The new optimum " + minMakeSpan);
            for (int i = 0; i < assignment.size(); i++) {
                System.out.print(assignment.get(i).getId() + " ");
            }
            System.out.println();
        }
        return minMakeSpan;
    }
}
