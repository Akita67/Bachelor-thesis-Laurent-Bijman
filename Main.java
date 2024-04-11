import java.util.Random;
public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        final int v = 4;
        final int max_edge = v*(v-1)/2;

        Graph graph = new Graph(v);
        final boolean randomgraph = true;

        // Random graph
        if(randomgraph) {
            int limit = rand.nextInt(1, max_edge);
            for (int i = 0; i < limit; i++){
                graph.addEdge(rand.nextInt(v), rand.nextInt(v), rand.nextInt(1, v));}
        }

        if(!randomgraph) {
            graph.addEdge(0, 1, 4);
            graph.addEdge(0, 2, 4);

            graph.addEdge(3, 1, 4);
            graph.addEdge(3, 2, 4);
        }

        graph.printGraph();
    }
}