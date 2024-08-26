public class Parcel extends Deliverable{

    @Override public int compareTo(Deliverable i) {
        int floorDiff = this.floor - i.myFloor();  // Don't really need this as only deliver to one floor at a time
        return (floorDiff == 0) ? this.room - i.myRoom() : floorDiff;
    }

    Parcel(int floor, int room, int arrival, int weight) {
        this.floor = floor;
        this.room = room;
        this.arrival = arrival;
        this.weight = weight;
    }

    public String toString() {
        return "Floor: " + floor + ", Room: " + room + ", Arrival: " + arrival + ", Weight: " + weight;
    }

    @Override int myFloor() { return this.floor; }
    @Override int myRoom() { return room; }
    @Override int myArrival() { return arrival; }

    @Override int myWeight() {return weight;}
}
