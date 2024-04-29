class Agent {
    Vertex currentPosition;
    Vertex destination;
    int id;
    double max_energy_tank = 100;
    double current_distance = 0;
    double charging_time = 5;
    boolean did_charge = false;

    public Agent(int id, Vertex currentPosition, Vertex destination) {
        this.id = id;
        this.currentPosition = currentPosition;
        this.destination = destination;
    }
    public int getId(){
        return id;
    }
    public void setDid_charge(boolean value){
        did_charge = value;
    }
}
