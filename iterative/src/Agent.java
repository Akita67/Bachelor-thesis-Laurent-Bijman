class Agent {
    Vertex currentPosition;
    Vertex startVertex;
    Vertex destination;
    int id;
    double max_energy_tank = 300;
    double current_distance = 0;
    double charging_time = 0;
    Vertex charging_station;
    boolean did_charge = false;

    public Agent(int id, Vertex currentPosition, Vertex destination) {
        this.id = id;
        this.currentPosition = currentPosition;
        this.startVertex = currentPosition;
        this.destination = destination;
    }
    public int getId(){
        return id;
    }
    public void setDid_charge(){
        this.did_charge = true;
    }
    public void setCharging_time(double value){ this.charging_time = value;}

    public void setCharging_station(Vertex value) {this.charging_station = value;}
    public void reset(){
        this.currentPosition = startVertex;
        this.current_distance = 0;
        this.charging_time = 0;
        this.did_charge = false;
    }
    public Vertex getCharging_station(){
        return this.charging_station;
    }
    public Vertex getStart_vertex(){
        return this.startVertex;
    }
    public Vertex getDestination_vertex(){
        return this.destination;
    }


}
