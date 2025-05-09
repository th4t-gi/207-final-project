
public class Pair<T> {
    private T p1;
    private T p2;

    public Pair(T input1, T input2) {
        p1 = input1;
        p2 = input2;
    }

    T getFirst(){
        return p1;
    }

    T getSecond(){
        return p2;
    }

    boolean isEqual(Pair other) {
        if(other.getFirst() == this.getSecond() &&
        other.getSecond() == this.getFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public String toString() {
        return "(" + getFirst() + ", " + getSecond() + ")";
    }

    public void switchOrder() {
        T temp = p1;
        p1 = p2;
        p2 = temp;
    }
}
