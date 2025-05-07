public class Agent {
    private boolean strategy = true; // true = cooperate, false = defect
    private double reinforcement = 0;
    private int id;
    private int k;

    public Agent(int id, int k){
        this.id = id;
        this.k = k;
    }

    public int getId() {
        return this.id;
    }

    
    public boolean getStrategy() {
        return this.strategy;
    }

    public void setStrategy(boolean strategy) {
        this.strategy = strategy;
    }

    public double getReinforcement() {
        return this.reinforcement;
    }

    public void addReinforcement(double n) {
        this.reinforcement += n/this.k;
    }

    public void setKValue(int n){
        this.k = n;
    }

    public int getKValue(){
        return this.k;
    }



}
