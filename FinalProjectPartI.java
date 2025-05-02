import java.util.*;




public class FinalProjectPartI {
    // network class --> builds object
    // each network has a list of nodes and a list of connections
    // network stores an array of pairs
    // number of nodes
    // new Network(nodeNumber, connnectionsArray)

    // N, p --> for each node, randomly create connections with each other node
    // 
    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args){
        System.out.println("Which type of network do you want to grow? Please enter an integer 1-3");
        System.out.println("1 = example network, 2 = randomly generated network, 3 = 2D4N regular lattice");
        int userInput = sc.nextInt();
        while(userInput < 1 || userInput > 3) {
            System.out.println("Input invalid. Please enter an integer 1-3");
            userInput = sc.nextInt();
        }
        switch(userInput) {
            case 1: optionOne();
            break;
            case 2: optionTwo();
            break;
            case 3: optionThree();
            break;
            default: System.out.println("Error in user input");
            break;
        }
        

        
    }

    public static void optionOne(){
        ArrayList<Pair<Integer>> arr1 = new ArrayList(Arrays.asList(new Pair(1, 4),
            new Pair(1,7),
            new Pair(1,10),
            new Pair(2,7),
            new Pair(2,9),
            new Pair(2,11),
            new Pair(4,5),
            new Pair(8,10),
            new Pair(9,10),
            new Pair(9,12)));

        Network network1 = new Network(12, arr1);
        network1.printNetwork("network1.txt");
    }

    public static void optionTwo(){
        System.out.println("Please enter number of nodes [between 10,1000]: ");
        int nodes = sc.nextInt();
        while (nodes < 10 || nodes > 1000) {
            System.out.println("Invalid input, please enter an integer between 10 and 1000: ");
            nodes = sc.nextInt();
        }
        
        System.out.printf("Please enter a probability (between 1/%d and 1):\n", nodes);
        double probability = sc.nextDouble();
        while (probability < 1.0/nodes || probability > 1) {
            System.out.printf("Invalid input, please enter a probablility between 1/%d and 1:\n", nodes);
            probability = sc.nextDouble();
        }

        ArrayList<Pair<Integer>> arr2 = new ArrayList();
        for (int i = 1; i <= nodes; i++) {
            for (int j = i+1; j <= nodes; j++) {
                double r = random.nextDouble(0, 1);

                if (r > probability) {
                    Pair newConnection = new Pair(i,j);
                    arr2.add(newConnection);
                }
            }
        }

        Network network2 = new Network(nodes, arr2);
        network2.printNetwork("network2.txt");
    }

    public static void optionThree(){
        System.out.println("Please enter number of nodes [between 9,1000]: ");
        int nodes = sc.nextInt();

        while (nodes < 9 || nodes > 1000) {
            System.out.println("Invalid input, please enter an integer between 9 and 1000: ");
            nodes = sc.nextInt();
        }

        int width;
        while ((width = canCreateSquare(nodes)) == 0) {
            nodes--;
        }
  
        int height = nodes/width;


        ArrayList<Pair<Integer>> connectionsArr = new ArrayList<>();

        for(int node = 1; node <= nodes; node++) {
            int row = ((node - 1) / width) + 1;
            int col = ((node - 1) % width) + 1;

            if (row < height) {
                connectionsArr.add(new Pair(node, node+width));
            } else {
                connectionsArr.add(new Pair(node, col));
            }
            
            if (col < width) {
                connectionsArr.add(new Pair(node, node+1));
            }  else {
                connectionsArr.add(new Pair(node, node-col+1));
            }
        }

        Network network3 = new Network(nodes, connectionsArr);
        network3.printNetwork("network3.txt");

    }

    private static int canCreateSquare(int n) {
        int i;
        for (i = 3; i <= Math.sqrt(n); i++) {
            if((n % i) == 0) {
                return i;
            }
        }
        return 0;
    }

}
