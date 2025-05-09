
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class FinalProjectPartII {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("What option do you want to run?[1-17]");
        int option = sc.nextInt() -1;
        //TODO Ensure that option is between 1 and 17

        String[] networkNames = {
                "network1.txt",
                "network-1000-15.txt",
                "network-1000-15.txt",
                "network-1000-5.txt",
                "network-1000-6.txt",
                "network-1500-3.txt",
                "network-1500-3.txt",
                "network-1500-9.txt",
                "network3.txt",
                "network3.txt",
                "network3.txt",
                "network3.txt",
                "network3.txt",
                "network3.txt",
                "network3.txt",
                "network3.txt",
                "network-1000-15.txt"
        };

        String[] outputNames = {
            "game1-output.txt",
            "game2-output.txt",
            "game3-output.txt",
            "game4-output.txt",
            "game5-output.txt",
            "game6-output.txt",
            "game7-output.txt",
            "game8-output.txt",
            "game9-output.txt",
            "game10-output.txt",
            "game11-output.txt",
            "game12-output.txt",
            "game13-output.txt",
            "game14-output.txt",
            "game15-output.txt",
            "game16-output.txt",
            "game17-output.txt",
        };

        Object[][] parameters = {
            {2, 0, 1.0, true, 0.5},//1
            {2, 500, 1.0, true, 0.1},//2
            {2, 500, 1.0, true, 0.9},//3
            {2, 500, 1.0, true, 0.6},//4
            {2, 500, 1.0, true, 0.6},//5
            {2, 700, 1.0, true, 0.3},//6
            {2, 700, 2.0, true, 0.3},//7
            {2, 700, 2.0, true, 0.3},//8
            {2, 500, 0.2, false, 0.1},//9
            {2, 500, 0.5, false, 0.1},//10
            {2, 500, 0.75, false, 0.1},//11
            {3, 500, 0.2, false, 0.1},//12
            {3, 500, 0.4, false, 0.5},//13
            {1, 500, 0.75, false, 0.9},//14
            {1, 500, 0.25, false, 0.9},//15
            {},//16
            {}//17
        };

        ArrayList<Pair<Integer>> connections = new ArrayList<>();
        int numNodes = 0;

        // process text file
        try (BufferedReader br = new BufferedReader(new FileReader(networkNames[option]))) {
            String line;
            while ((line = br.readLine()) != null) {
                int node1 = Integer.parseInt(line.split(" ")[0]);
                int node2 = Integer.parseInt(line.split(" ")[1]);

                if (node1 > numNodes) {
                    numNodes = node1;
                } else if (node2 > numNodes) {
                    numNodes = node2;
                }

                if (node2 != -1) {
                    connections.add(new Pair<>(node1-1, node2-1));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        Agent[] agents = new Agent[numNodes];
        for (int i = 0; i < numNodes; i++) {
            agents[i] = new Agent(i, 0);
        }

        Network<Agent> network = new Network<>();

        for (Pair<Integer> connection : connections) {
            System.out.println(connection);
            network.addConnection(agents[connection.getFirst()], agents[connection.getSecond()]);
        }

        if (option == 15 || option == 16) {
            //TODO Get user input and create environment
        } else {
            Environment env = new Environment(
                agents, 
                network, 
                (int) parameters[option][0],
                (int) parameters[option][1],
                (double) parameters[option][2],
                (boolean) parameters[option][3],
                (double) parameters[option][4]);

            env.runGame();
            env.print("game1-output.txt");
        }

    }

}
