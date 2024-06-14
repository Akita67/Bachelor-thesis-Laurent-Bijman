import java.util.*;

public class SelfishNashEquilibrium {

    public static void main(String[] args) {

        Graph graph = new Graph();
// Create vertices
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);v4.charging_station=true;
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);
        Vertex v8 = new Vertex(8);v8.charging_station=true;
        Vertex v9 = new Vertex(9);
        Vertex v10 = new Vertex(10);
        Vertex v11 = new Vertex(11);
        Vertex v12 = new Vertex(12);
        Vertex v13 = new Vertex(13);
        Vertex v14 = new Vertex(14);v14.charging_station=true;
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
        for (int i = 1; i < 2; i++) {
            agents.add(new Agent(i, v1, v6));
        }
        for (int i = 2; i < 3; i++) {
            agents.add(new Agent(i, v1, v7));
        }


        List<Vertex> list_chargingS = graph.getChargingStations();
        List<Vertex> currentAssignment = new ArrayList<>();

        simple_Assign(currentAssignment,agents,list_chargingS);

        int count = 0;
        double value;
        boolean flag = false;
        Vertex stockremain;
        List<Vertex> minList = new ArrayList<>(currentAssignment);
        List<Double> minMakeSpan = new ArrayList<>();
        for (int i = 0; i < agents.size(); i++) {
            minMakeSpan.add(Double.MAX_VALUE);
        }


        while(count < agents.size()){
            System.out.println(count);
            boolean flagremain = false;
            stockremain = currentAssignment.get(count);
            for (Vertex charge: list_chargingS) {
                currentAssignment.set(count, charge);
                value = simple_Game(currentAssignment,agents,graph,minMakeSpan,agents.get(count));
                //System.out.println(value);
                if(value < minMakeSpan.get(count)){
                    minMakeSpan.set(count,value);
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

        System.out.println("We reach an equilibrium with the list of " + minMakeSpan);
        for (int i = 0; i < minList.size(); i++) {
            System.out.print(minList.get(i).getId() + " ");
        }
        System.out.println("\n" + "And with the makespan of " + minMakeSpan.stream().max(Comparator.naturalOrder()).get());
    }



    public static void simple_Assign(List<Vertex> currentAssignment, List<Agent> agents, List<Vertex> list_chargingS){
        for (int i=0 ; i<agents.size() ; i++) {
            currentAssignment.add(list_chargingS.get(0));
        }
    }
    public static double simple_Game(List<Vertex> assignment, List<Agent> agents, Graph graph, List<Double> minMakeSpan, Agent currentAgent){
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
        double max_time = currentAgent.current_distance;
        //System.out.println("The makespan of this problem is " + max_time);
        if(max_time<minMakeSpan.get(currentAgent.getId()-1)){
            //minMakeSpan.set(currentAgent.getId()-1, max_time);
            System.out.println("The new selfish improvement from " + minMakeSpan.get(currentAgent.getId()-1) + " to " + max_time);
            for (int i = 0; i < assignment.size(); i++) {
                System.out.print(assignment.get(i).getId() + " ");
            }
            System.out.println();
        }
        return max_time;
    }
}
