import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class GameManager {
    // Method to determine which agent moves first
    public Agent determineNextMove(List<Agent> agents, Graph graph) {
        Agent nextMoveAgent = null;
        double minTotalCost = Double.MAX_VALUE;
        List<Vertex> minpath = null;
        double totalWaitingTime = 0;
        double chargingTime = 0;
        double nextMoveCost = 0;
        double totalCost = 0;

        for (Agent agent : agents) {
            if(agent.currentPosition.equals(agent.destination)){
                continue;
            }
            else if(agent.currentPosition.charging_station && !agent.did_charge){
                totalWaitingTime = getTotalWaitingTime(agents, agent);
                chargingTime = getChargingTime(agent, agent.currentPosition);
                totalCost = agent.current_distance + totalWaitingTime + chargingTime;
                minTotalCost = totalCost;
                nextMoveAgent = agent;
                minpath = null;
                agent.setDid_charge();
                break;
            }
            else{
                List<Vertex> path = dijkstra(graph, agent.currentPosition, agent.destination, agent, agents);
                nextMoveCost = getNextMoveCost(path, graph);
                totalCost = agent.current_distance + nextMoveCost;
                if (totalCost < minTotalCost) {
                    minTotalCost = totalCost;
                    nextMoveAgent = agent;
                    minpath = path;
                }
            }
            //System.out.println("nextMoveCost " + nextMoveCost);
            //System.out.println("totalWaitingTime " + totalWaitingTime);
            //System.out.println("chargingTime "+ chargingTime );

        }
        if(minpath == null){
            System.out.println("the agent " + (nextMoveAgent.getId()) + " stays in " + nextMoveAgent.currentPosition.id);
        }
        else{
            System.out.println("the agent " + (nextMoveAgent.getId()) + " goes to " + (minpath.get(1).getId()));
            nextMoveAgent.currentPosition = minpath.get(1);
        }
        System.out.println("The cost of the agent is " + (minTotalCost));
        nextMoveAgent.current_distance = minTotalCost;

        return nextMoveAgent;
    }

    // Method to calculate the distance between two vertices
    private List<Vertex> dijkstra(Graph graph, Vertex source, Vertex destination, Agent agent, List<Agent> agents){


        return graph.shortestPath(source, destination, agent, agents);
    }
    private double getNextMoveCost(List<Vertex> path, Graph graph) {

        return graph.getEdge(path.get(0),path.get(1)).getWeight();
    }
    private double getTotalWaitingTime(List<Agent> agents, Agent agent){ //TODO if someone already charged and it's in the charging node(for passing) should not take his charging time into consideration
        List<Agent> list = new ArrayList<>();
        double total_waiting = 0;
        double waiting = 0;
        for(Agent a : agents){
            if(a.currentPosition.equals(agent.currentPosition)){
                list.add(a);
            }
        }
        if(list.size()>1){
            double smallest_distance = Double.MAX_VALUE;
            for(Agent a : list){
                total_waiting += a.charging_time;
                if(a.current_distance - a.charging_time<smallest_distance)
                    smallest_distance = a.current_distance - a.charging_time;
            }
            // TODO need to take the smallest current_distance in the list
            System.out.println("those are the values for the waiting sum");
            System.out.println(smallest_distance);
            System.out.println(total_waiting);
            System.out.println(agent.current_distance);
            waiting = Math.max((smallest_distance + total_waiting) - agent.current_distance,0);
        }
        return waiting;
    }
    private double getChargingTime(Agent agent, Vertex node){
        double charging_t = 0;
        if(node.charging_station){
            if(node.fast_charging){charging_t = (agent.current_distance/agent.max_energy_tank)*30;}
            else{charging_t = (agent.current_distance/agent.max_energy_tank)*60;}
            agent.setCharging_time(charging_t);
        }
        return charging_t;
    }
}
