public class Letter extends Deliverable{

    @Override public int compareTo(Deliverable i) {
        int floorDiff = this.myFloor() - i.myFloor();// Don't really need this as only deliver to one floor at a time
        return (floorDiff == 0) ? this.myRoom() - i.myRoom(): floorDiff;
    }

    Letter(int floor, int room, int arrival) {
        super(floor, room, arrival);
    }

    public String toString() {
        return "Floor: " + myFloor() + ", Room: " + myRoom() + ", Arrival: " + myArrival() + ", Weight: " + 0;
    }
}
