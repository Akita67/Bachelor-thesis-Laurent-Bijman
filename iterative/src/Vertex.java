import java.util.LinkedList;
import java.util.List;

class Vertex {
    int id;
    boolean charging_station = false;
    boolean fast_charging = false;
    List<Double> queue = new LinkedList<>();

    public Vertex(int id) {
        this.id = id;
        // Initialize other properties
    }
}