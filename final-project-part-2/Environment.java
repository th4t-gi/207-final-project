import java.util.LinkedList;
import java.util.Random;

public class Environment {
    private double p;
    private int b;
    private Threshold threshold;
    private boolean relativeToK;
    private double m;

    private Agent[] agents;
    private Network<Agent> network;
    private Random random = new Random(System.currentTimeMillis());

    private boolean gameOver;
    
    public Environment(Agent[] agents, Network<Agent> network, double p, int b, int h, Threshold t,
     boolean relativeToK, double m) {
        this.network = network;
        this.agents = agents;
        this.p = p;
        this.b = b;
        this.threshold = t;
        this.relativeToK = relativeToK;
        this.m = m;
        

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
            for(Agent a : this.agents) {
                LinkedList<Agent> neighbors = network.getNetworkMap().get(a);

                for(Agent n : neighbors) {
                    //check for same r value/strategy
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
            //TODO: If an agent is eliminated, gameover = false
            if (a.getReinforcement() < this.threshold.t(a)) {
                this.network.removeConnectionsOf(a);
            }

            //TODO: If an agent switches strategy gameover = false

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
                    a.setStrategy(maxAgent.getStrategy());
                }
            }
        }
    }



    public void print() {

    }
}

interface Threshold {
    double t(Agent a);
} 