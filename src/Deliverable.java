public abstract class Deliverable implements Comparable<Deliverable> {
    protected int floor;
    protected int room;
    protected int arrival;
    protected int weight;

    abstract int myRoom();
    abstract int myFloor();
    abstract int myArrival();
    abstract int myWeight();
}
