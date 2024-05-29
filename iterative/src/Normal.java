import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Normal {
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

        List<List<Agent>> groupAgent = new ArrayList<>();
        List<Agent> agents1 = new ArrayList<>();
        agents1.add(new Agent(1, v1, v10));groupAgent.add(agents1);
        List<Agent> agents2 = new ArrayList<>();
        agents2.add(new Agent(2, v11, v5));groupAgent.add(agents2);
        List<Agent> agents3 = new ArrayList<>();
        agents3.add(new Agent(3, v6, v15));groupAgent.add(agents3);

        List<Vertex> list_chargingS = graph.getChargingStations();
        List<List<Vertex>> groupChar = new ArrayList<>();

        for(List<Agent> agents: groupAgent) {
            List<Vertex> accepted_chargingS = new ArrayList<>();
            List<Double> distances = new ArrayList<>();
            double min_time = Double.MAX_VALUE;
            for (Vertex station : list_chargingS) {
                boolean stop = false;
                agents.get(0).reset();
                agents.get(0).setCharging_station(station);

                GameManager game = new GameManager();
                while (!stop) {
                    game.BrutdetermineNextMove(agents, graph);
                    if (agents.get(0).currentPosition.equals(agents.get(0).destination)) {
                        stop = true;
                        distances.add(agents.get(0).current_distance);
                    }
                }
                if (distances.get(distances.size() - 1) < min_time) {
                    min_time = distances.get(distances.size() - 1);
                }
            }
            List<DistanceChargingPair> pairs = new ArrayList<>();
            for (int i = 0; i < distances.size(); i++) {
                if (Math.abs(min_time - distances.get(i)) <= 50) {
                    pairs.add(new DistanceChargingPair(distances.get(i), list_chargingS.get(i)));
                }
            }
            Collections.sort(pairs, Comparator.comparingDouble(pair -> pair.distance));
            for (DistanceChargingPair pair : pairs) {
                accepted_chargingS.add(pair.chargingStation);
            }
            groupChar.add(accepted_chargingS);
        }
        for (List<Vertex> a : groupChar) {
            System.out.println("Group :");
            for (Vertex charg : a) {
                System.out.println(charg.id);
            }
        }
        // TODO here we need to add the charging stations
        List<List<Vertex>> result = new ArrayList<>();
        List<Vertex> specific = new ArrayList<>();
        List<Agent> agents = new ArrayList<>();
        int count=1;
        int temp = 1;
        for (int i = 1; i < temp+1; i++) {
            agents.add(new Agent(i, v1, v10));
            specific.add(groupChar.get(0).get(count%groupChar.get(0).size()));
            count++;
        }
        count=1;
        for (int i = temp+1; i < 2*temp+1; i++) {
            agents.add(new Agent(i, v11, v5));
            specific.add(groupChar.get(1).get(count%groupChar.get(1).size()));
            count++;
        }
        count=1;
        for (int i = 2*temp+1; i < 3*temp+1; i++) {
            agents.add(new Agent(i, v6, v15));
            specific.add(groupChar.get(2).get(count%groupChar.get(2).size()));
            count++;
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
            System.out.println("This is the makespan of a normal behavior " + max_time);
        }
    }
}