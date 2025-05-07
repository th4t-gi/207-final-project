
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class FinalProjectPartII {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){

        ArrayList<Pair<Integer>> connections = new ArrayList<>();
        int numNodes = 0;

        //process text file
        try(BufferedReader br = new BufferedReader(new FileReader("networks.txt"))){
            String line;
            while((line = br.readLine()) != null) {
                int node1 = Integer.parseInt(line.split(" ")[0]);
                int node2 = Integer.parseInt(line.split(" ")[1]);

                if (node1 > numNodes) {
                    numNodes = node1;
                } else if (node2 > numNodes) {
                    numNodes = node2;
                }

                if (node2 != -1) {
                    connections.add(new Pair<>(node1, node2));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        Network<Integer> network = new Network<>(numNodes, connections);


        //list of 17 environment instances
        


    }


}
