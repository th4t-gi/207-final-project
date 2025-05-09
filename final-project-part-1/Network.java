
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// import java.util.ArrayList;

public class Network {
    // network class --> builds object
    // each network has a list of nodes and a list of connections
    // network stores an array of pairs
    // number of nodes
    // new Network(nodeNumber, connnectionsArray)
    // new Network(10, generateArray(10))
    private ArrayList<Pair<Integer>> networkArray;
    private int nodes; 

    public Network(int n, ArrayList<Pair<Integer>> connectionsArray) {
        networkArray = connectionsArray;
        nodes = n;
        for (Pair<Integer> connection : networkArray) {
            if(connection.getFirst() > connection.getSecond()) {
                connection.switchOrder();
            }
        }
        for (int i = 1; i <= nodes; i++) {
            if(getConnections(i).size() == 0) {
                networkArray.add(new Pair<>(i, -1));
            }
        }
        networkArray.sort((PairA, PairB) -> {return PairA.getFirst() - PairB.getFirst(); });
    }

    public void printNetwork(String fileName){
        try{
            PrintWriter out = new PrintWriter(fileName, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            for(Pair<Integer> connection : networkArray) {
                sb.append(connection.getFirst() + " " + connection.getSecond() + "\n");
            }
            out.write(sb.toString());

            out.close();
        }
        catch(IOException e) {
            System.out.println("Error writing to file" + e.getMessage());
        }
    }

    public ArrayList<Pair<Integer>> getConnections(int n) {
        ArrayList<Pair<Integer>> nConnections = new ArrayList();
        for (Pair<Integer> connection: networkArray) {
            if(connection.getFirst() == n) {
                nConnections.add(connection);
            }
            if(connection.getSecond() == n) {
                nConnections.add(connection);
            }
        }

        return nConnections;
    }

}
