class Agent {
    Vertex currentPosition;
    Vertex destination;
    int id;
    double energy_tank = 350;
    double current_distance = 0;
    boolean did_charge = false;

    public Agent(int id, Vertex currentPosition, Vertex destination) {
        this.id = id;
        this.currentPosition = currentPosition;
        this.destination = destination;
    }

}
