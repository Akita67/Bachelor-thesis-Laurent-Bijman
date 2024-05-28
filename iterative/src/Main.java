import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        boolean stop = false;

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

        for (int i = 1; i < 41; i++) {
            agents.add(new Agent(i, groningen, utrecht));
        }
        for (int i = 41; i < 81; i++) {
            agents.add(new Agent(i, maastricht, utrecht));
        }
        for (int i = 81; i < 121; i++) {
            agents.add(new Agent(i, breda, utrecht));
        }
        for (int i = 121; i < 161; i++) {
            agents.add(new Agent(i, haarlem, utrecht));
        }
        for (int i = 161; i < 201; i++) {
            agents.add(new Agent(i, enschede, utrecht));
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
            if(agent.current_distance>max_time){
                max_time = agent.current_distance;
            }
        }
        System.out.println("The makespan of this problem is " + max_time);
    }
}