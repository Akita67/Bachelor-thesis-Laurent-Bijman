import java.util.Queue;
import java.util.LinkedList;
class Vertex {
    protected int id;
    protected boolean charging_station = false;
    protected boolean fast_charging = false;

    protected Queue<Agent> queue = new LinkedList<>();
    public Vertex(int id){
        this.id = id;

    }
}