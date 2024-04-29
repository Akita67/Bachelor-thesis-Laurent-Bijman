import java.util.NoSuchElementException;

class Edge {
    Vertex source;
    Vertex destination;
    double weight; // Cost of traversing this edge

    public Edge(Vertex source, Vertex destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }
}