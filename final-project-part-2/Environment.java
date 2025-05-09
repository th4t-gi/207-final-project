import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Environment {
    private int b;
    private int h;
    private Threshold t;
    private double m;
    private int numAgents;
    private int numRounds = 0;
    private int roundsSteady;

    // private Agent[] agents;
    private Network<Agent> network;
    private Random random = new Random(System.currentTimeMillis());

    private boolean gameOver = false;
    
    public Environment(int numAgents, ArrayList<int[]> connections, int b, int h, Threshold t, double m) {
        this.network = new Network<Agent>();
        this.numAgents = numAgents;
        this.b = b;
        this.h = h;
        this.t = t;
        this.m = m;
        // this.startNumAgents = agents.size(); 
        // this.endNumAgents = agents.size();
        this.numRounds = 0;
        this.roundsSteady = 0;

        //make agents
        Agent[] agents = new Agent[numAgents];
        for (int i = 0; i < numAgents; i++) {
            agents[i] = new Agent(i, 0);
            this.network.initNode(agents[i]);
        }

        //fill in network hashmap 
        for (int[] connection : connections) {
            this.network.addConnection(agents[connection[0]], agents[connection[1]]);
        }

        //Remove nodes without any neighbors
        for(Agent a : agents) {
            if(this.network.get(a).isEmpty()){
                this.network.remove(a);
            }
        }
        
        //set the k values for all of the agents
        agents[h].setStrategy(false);
        for (Agent a : this.network.keySet()) {
            a.setKValue(this.network.get(a).size());
        }
    }

    public void runGame(){
        while(!gameOver && this.numAgents > 0 && this.numRounds < 100) {
            runRound();
            this.numRounds += 1;
            System.out.println("I did a round! " + this.numRounds);
            

        }
    }

    public boolean checkIsSteady() {
        this.roundsSteady += 1;
        for(Agent a : this.network.keySet()) {    
            for(Agent n : this.network.get(a)) {
                //check for same r value/strategy
                if(!(a.getReinforcement() == n.getReinforcement() || a.getStrategy() == n.getStrategy())){
                    this.gameOver = false;
                    this.roundsSteady = 0;
                    return true;
                }
            }
        }
        return true;
    }

    public void runRound() {

        for (Agent a : this.network.keySet()) {
            for (Agent n : this.network.get(a)) {
                if (n.getId() > a.getId()) {
                    if (n.getStrategy() && a.getStrategy()) {
                        //cooperate
                        a.addReinforcement(1);
                        n.addReinforcement(1);
                    } else {
                        if (!n.getStrategy()) {
                            //n is defective
                            n.addReinforcement(this.b);
                        } else if (!a.getStrategy()) {
                            //a is defective
                            a.addReinforcement(this.b);
                        }
                    } 
                }
            }
        }

        //eliminate agents
        for (Agent a : this.network.keySet()) {
            if (a.getReinforcement() < this.t.t(a)) {
                this.network.remove(a);
                this.gameOver = false;
                System.out.println("I kill a agent!" + this.network.keySet().size());
                System.out.println("Agent ID: " + a.getId());
            }

            //imitation
            if (random.nextDouble(1) < this.m) {
                LinkedList<Agent> neighbors = network.get(a);

                //check for higher reinforcement value among neighbors
                double maxR = a.getReinforcement();
                Agent maxAgent = a;
                for(Agent n : neighbors) {
                    if(n.getReinforcement() > maxR) {
                        maxR = n.getReinforcement();
                        maxAgent = n;
                    }
                }

                //set strategy
                if (!maxAgent.equals(a) && maxAgent.getStrategy() != a.getStrategy()) {
                    this.gameOver = false;
                    a.setStrategy(maxAgent.getStrategy());
                }
            }
        }
    }

    public Agent getMaxKId(){
        Agent maxId = this.network.keySet().toArray(new Agent[this.network.size()])[0];
        for(Agent a : this.network.keySet()) {
            if (a.getKValue() > maxId.getKValue()){
                maxId = a;
            }
        }
        return maxId;
    }

    public Agent getMinKId(){
        Agent minId = this.network.keySet().toArray(new Agent[this.network.size()])[0];
        for(Agent a: this.network.keySet()) {
            if (a.getKValue() < minId.getKValue()){
                minId = a;
            }
        }
        return minId;
    }

    public double percentEliminated(){
        return (this.network.size() / this.numAgents);
    }

    public int numDefectors(){
        int sum = 0;
        for(Agent a: this.network.keySet()){
            if(a.getStrategy() == false){
                sum += 1;
            }
        }
        return sum;
    }

    public int numCooperators(){
        int sum = 0;
        for(Agent a: this.network.keySet()){
            if(a.getStrategy() == true){
                sum += 1;
            }
        }
        return sum;
    }

    public void print(String filename) {
        System.out.println("The initial number of agents is: " + this.numAgents);
        System.out.println("The ID of the agent with the largest k is " + getMaxKId().getId() + ".");
        System.out.println("Its k value is " + getMaxKId().getKValue() + ".");
        System.out.println("The ID of the agent with the smallest k is " + getMinKId().getId() + ".");
        System.out.println("Its k value is " + getMinKId().getKValue() + ".");

        System.out.println("The parameters used were:");
        System.out.println(this.numAgents + " agents, " + "b = " + this.b + ", h = " + this.h +
        ", T = " + this.t + ", m = " + this.m + ".");

        try {
            PrintWriter out = new PrintWriter(filename, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            int edges = 0;
            sb.append("Percentage of Eliminated Agents: ").append(percentEliminated());
            sb.append("*Vertices ").append(this.network.size()).append("\n");
            for (Agent a : this.network.keySet()){
                LinkedList<Agent> neighbors = network.get(a);
                if (!(neighbors == null)) {
                    sb.append(a.getId()).append("\"").append(a.getId()).append("\"\n");
                    edges += network.get(a).size();
                }
            }
            sb.append("*Edges ").append(edges).append("\n");
            for (Agent a : this.network.keySet()){
                LinkedList<Agent> neighbors = network.get(a);
                if (!(neighbors == null)) {
                    for (Agent n: neighbors) {
                        sb.append(a.getId() + n.getId());
                    }
                }
            }
            out.println(sb.toString());
            out.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        
    }
}


interface Threshold {
    double t(Agent a);
} 