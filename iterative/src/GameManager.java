import java.util.List;
import java.util.Map;

class GameManager {
    // Method to determine which agent moves first
    public Agent determineNextMove(List<Agent> agents, Graph graph) {
        Agent nextMoveAgent = null;
        double minTotalCost = Double.MAX_VALUE;
        List<Vertex> minpath = null;

        for (Agent agent : agents) {
            if(agent.currentPosition.equals(agent.destination)){
                continue;
            }
            // TODO in here we need to adjust the charging and waiting time
            List<Vertex> path = dijkstra(graph, agent.currentPosition, agent.destination, agent, agents);
            double nextMoveCost = getNextMoveCost(path, graph);
            double totalWaitingTime = getTotalWaitingTime(agents,path.get(0), agent);
            double chargingTime = getChargingTime(agent, path.get(0));
            //System.out.println("nextMoveCost " + nextMoveCost);
            //System.out.println("totalWaitingTime" + totalWaitingTime);
            //System.out.println("chargingTime "+ chargingTime );
            double totalCost = agent.current_distance + nextMoveCost + totalWaitingTime + chargingTime;

            if (totalCost < minTotalCost) {
                minTotalCost = totalCost;
                nextMoveAgent = agent;
                minpath = path;
            }
        }
        System.out.println("the agent " + (nextMoveAgent.getId()) + " goes to " + (minpath.get(1).getId()));
        System.out.println("The cost of the agent is " + (minTotalCost));
        nextMoveAgent.current_distance = minTotalCost;
        nextMoveAgent.currentPosition = minpath.get(1);
        return nextMoveAgent;
    }

    // Method to calculate the distance between two vertices
    private List<Vertex> dijkstra(Graph graph, Vertex source, Vertex destination, Agent agent, List<Agent> agents){


        return graph.shortestPath(source, destination, agent, agents);
    }
    private double getNextMoveCost(List<Vertex> path, Graph graph) {

        return graph.getEdge(path.get(0),path.get(1)).getWeight();
    }
    private double getTotalWaitingTime(List<Agent> agents, Vertex destination, Agent agent){
        List<Agent> list = null;
        double total_waiting = 0;
        double waiting = 0;
        for(Agent a : agents){
            //TODO curious to see if there is not a charging station at the end if it will not add anything
            // I hope it will have a empty list and just return waiting 0
            if(a.currentPosition.equals(destination.charging_station)){
                list.add(a);
            }
        }
        if(list != null){

            for(Agent a : list){
                total_waiting += a.charging_time;
            }
            waiting = Math.max((list.get(0).current_distance + total_waiting) - agent.current_distance,0);
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
