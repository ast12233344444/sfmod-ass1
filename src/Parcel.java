// [Mon 15:15] Team 03
public class Parcel extends Deliverable{
    private final int weight;

    @Override public int compareTo(Deliverable i) {
        int floorDiff = this.myFloor() - i.myFloor();  // Don't really need this as only deliver to one floor at a time
        return (floorDiff == 0) ? this.myRoom() - i.myRoom() : floorDiff;
    }

    Parcel(int floor, int room, int arrival, int weight) {
        super(floor, room, arrival);
        this.weight = weight;
    }

    public String toString() {
        return "Floor: " + myFloor() + ", Room: " + myRoom() + ", Arrival: " + myArrival() + ", Weight: " + myWeight();
    }

    @Override public int myWeight() {
        return weight;
    }
}
