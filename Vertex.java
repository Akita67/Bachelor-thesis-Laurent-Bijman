import java.io.Serializable;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
class Vertex {
    protected int id;
    protected boolean charging_station = false;
    protected boolean fast_charging = false;
    protected int time_first = 0;

    protected List<Integer> queue = new LinkedList<>();
    public Vertex(int id){
        this.id = id;

    }
}