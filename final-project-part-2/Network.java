
import java.util.*;

// import java.util.ArrayList;

public class Network<T> {
    // network class --> builds object
    // each network has a list of nodes and a list of connections
    // network stores an array of pairs
    // number of nodes
    // new Network(nodeNumber, connnectionsArray)
    // new Network(10, generateArray(10))
    private Map<T, LinkedList<T>> networkMap;

    public Network(int n, ArrayList<Pair<T>> connectionsArray) {

        networkMap = new HashMap<>();

        for (Pair<T> connection : connectionsArray) {
            this.addConnection(connection.getFirst(), connection.getSecond());
        }
    }

    public void addConnection(T node1, T node2) {
        this.networkMap.get(node1).add(node2);
        this.networkMap.get(node2).add(node1);
    }

    public void removeConnection(T node){
        for (T n : this.networkMap.get(node)) {
            this.networkMap.get(n).remove(node);
        }
        this.networkMap.get(node).clear();
    }

    public Map<T, LinkedList<T>> getNetworkMap() {
        return this.networkMap;
    }

}
