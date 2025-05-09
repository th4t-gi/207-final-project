import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Random;

public class Environment {
    private int b;
    private int h;
    private double t;
    private boolean relativeToK;
    private double m;
    private int startNumAgents;
    private int endNumAgents;
    private int numRounds;
    private int roundsSteady;

    private Agent[] agents;
    private Network<Agent> network;
    private Random random = new Random(System.currentTimeMillis());

    private boolean gameOver;
    
    public Environment(Agent[] agents, Network<Agent> network, int b, int h, double t,
     boolean relativeToK, double m) {
        this.network = network;
        this.agents = agents;
        this.b = b;
        this.h = h;
        this.t = t;
        this.relativeToK = relativeToK;
        this.m = m;
        this.startNumAgents = agents.length; 
        this.endNumAgents = agents.length;
        this.numRounds = 0;
        this.roundsSteady = 0;

        //make agents
        

        //set the k values for all of the agents
        agents[h].setStrategy(false);
        for (Agent a : this.agents) {
            a.setKValue((network.getNetworkMap().get(a)).size());
        }
    }

    public void runGame(){
        this.gameOver = false;
        while(!gameOver) {
            gameOver = true;
            runRound();
            this.numRounds += 1;
            if (gameOver == true) { 
                this.roundsSteady = 1;
                for(Agent a : this.agents) {
                    LinkedList<Agent> neighbors = network.getNetworkMap().get(a);
    
                    for(Agent n : neighbors) {
                        //check for same r value/strategy
                        if(!(a.getReinforcement() == n.getReinforcement() || a.getStrategy() == n.getStrategy())){
                            gameOver = false;
                            this.roundsSteady = 0;
                            break;
                        }
                    }
                }
            }
            
        }
    }

    public void runRound() {

        for (Agent a : this.agents) {
            LinkedList<Agent> neighbors = network.getNetworkMap().get(a);

            for (Agent n : neighbors) {
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
        for (Agent a : this.agents) {
            if(relativeToK) {
                if (a.getReinforcement() < t/a.getKValue()) {
                    this.network.removeConnection(a);
                    this.gameOver = false;
                    this.endNumAgents -= 1;
                }
            } else {
                if(a.getReinforcement() < t) {
                    this.network.removeConnection(a);
                    this.gameOver = false;
                    this.endNumAgents -= 1;
                }
            }

            //imitation
            if (random.nextDouble(1) < this.m) {
                LinkedList<Agent> neighbors = network.getNetworkMap().get(a);

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
        Agent maxId = agents[0];
        for(Agent a: agents) {
            if (a.getKValue() > maxId.getKValue()){
                maxId = a;
            }
        }
        return maxId;
    }

    public Agent getMinKId(){
        Agent minId = agents[0];
        for(Agent a: agents) {
            if (a.getKValue() < minId.getKValue()){
                minId = a;
            }
        }
        return minId;
    }

    public double percentEliminated(){
        return (this.endNumAgents / this.startNumAgents);
    }

    public int numDefectors(){
        int sum = 0;
        for(Agent a: agents){
            if(a.getStrategy() == false){
                sum += 1;
            }
        }
        return sum;
    }

    public int numCooperators(){
        int sum = 0;
        for(Agent a: agents){
            if(a.getStrategy() == true){
                sum += 1;
            }
        }
        return sum;
    }

    public void print(String filename) {
        System.out.println("The initial number of agents is: " + this.startNumAgents);
        System.out.println("The ID of the agent with the largest k is " + getMaxKId().getId() + ".");
        System.out.println("Its k value is " + getMaxKId().getKValue() + ".");
        System.out.println("The ID of the agent with the smallest k is " + getMinKId().getId() + ".");
        System.out.println("Its k value is " + getMinKId().getKValue() + ".");

        System.out.println("The parameters used were:");
        System.out.println(this.startNumAgents + " agents, " + "b = " + this.b + ", h = " + this.h +
        ", T = " + this.t + ", m = " + this.m + ".");

        try {
            PrintWriter out = new PrintWriter(filename, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            int edges = 0;
            sb.append("Percentage of Eliminated Agents: ").append(percentEliminated());
            sb.append("*Vertices ").append(this.endNumAgents).append("\n");
            for (Agent a : agents){
                LinkedList<Agent> neighbors = network.getNetworkMap().get(a);
                if (!(neighbors == null)) {
                    sb.append(a.getId()).append("\"").append(a.getId()).append("\"\n");
                    edges += network.getNetworkMap().get(a).size();
                }
            }
            sb.append("*Edges ").append(edges).append("\n");
            for (Agent a : agents){
                LinkedList<Agent> neighbors = network.getNetworkMap().get(a);
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
