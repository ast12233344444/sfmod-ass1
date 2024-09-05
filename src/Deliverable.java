public abstract class Deliverable implements Comparable<Deliverable> {
    private static final int DEFAULT_WEIGHT = 0;

    private final int floor;
    private final int room;
    private final int arrival;

    Deliverable(int floor, int room, int arrival) {
        this.floor = floor;
        this.room = room;
        this.arrival = arrival;
    }

    public int myFloor() { return this.floor; }
    public int myRoom() { return room; }
    public int myArrival() { return arrival; }
    public int myWeight() {return DEFAULT_WEIGHT;}
}
