
import java.util.*;

// import java.util.ArrayList;

public class Network<T> extends HashMap<T, LinkedList<T>> {
    // network class --> builds object
    // each network has a list of nodes and a list of connections
    // network stores an array of pairs
    // number of nodes
    // new Network(nodeNumber, connnectionsArray)
    // new Network(10, generateArray(10))
    // private Map<T, LinkedList<T>> networkMap;

    public Network() {
        super();
    }

    public Network(ArrayList<T[]> connectionsArray) {
        super();

        for (T[] connection : connectionsArray) {
            this.addConnection(connection[0], connection[0]);
        }
    }

    public void initNode(T node) {
        this.put(node, new LinkedList<>());
    }

    public void removeEmpty(){
        Set<T> keys = this.keySet();

        for(T key : keys) {
            if(this.get(key).isEmpty()){
                this.remove(key);
            }
        }
    }

    public void addConnection(T node1, T node2) {
        this.get(node1).add(node2);
        this.get(node2).add(node1);
    }

    public void removeConnectionsOf(T node){
        for (T n : this.get(node)) {
            this.get(n).remove(node);
        }
        this.get(node).clear();
    }

}
