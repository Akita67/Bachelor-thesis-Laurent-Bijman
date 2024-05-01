class Agent {
    Vertex currentPosition;
    Vertex destination;
    int id;
    double max_energy_tank = 100;
    double current_distance = 0;
    double charging_time = 0;
    Vertex charging_station;
    boolean did_charge = false;

    public Agent(int id, Vertex currentPosition, Vertex destination) {
        this.id = id;
        this.currentPosition = currentPosition;
        this.destination = destination;
    }
    public int getId(){
        return id;
    }
    public void setDid_charge(){
        did_charge = true;
    }
    public void setCharging_time(double value){ charging_time = value;}

    public void setCharging_station(Vertex value) {charging_station = value;}
}
