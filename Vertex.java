import java.io.Serializable;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
class Vertex {
    protected int id;
    protected boolean charging_station = false;
    protected boolean fast_charging = false;
    protected int time_first = 0;
    protected boolean EV_has_charged = false;

    protected List<Integer> queue = new LinkedList<>();
    public Vertex(int id){
        this.id = id;

    }
    public void set_EV_has_charged(boolean value){
        EV_has_charged = value;
    }
}