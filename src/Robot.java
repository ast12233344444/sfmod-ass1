import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Robot {
    private static int count = 1;
    private static int capacity;
    final private String id;
    private int floor;
    private int room;
    private int usedCapacity;
    final private MailRoom mailroom;
    final private List<Deliverable> deliverables = new ArrayList<>();

    public String toString() {
        return "Id: " + id + " Floor: " + floor + ", Room: " + room + ", #items: " + numItems() + ", Load: " + 0 ;
    }

    Robot(MailRoom mailroom, int given_capacity) {
        this.id = "R" + count++;
        this.mailroom = mailroom;
        capacity = given_capacity;
        usedCapacity = 0;
    }

    int getFloor() { return floor; }
    int getRoom() { return room; }
    boolean isEmpty() { return deliverables.isEmpty(); }

    public void place(int floor, int room) {
        Building building = Building.getBuilding();
        building.place(floor, room, id);
        this.floor = floor;
        this.room = room;
    }

    private void move(Building.Direction direction) {
        Building building = Building.getBuilding();
        int dfloor, droom;
        switch (direction) {
            case UP    -> {dfloor = floor+1; droom = room;}
            case DOWN  -> {dfloor = floor-1; droom = room;}
            case LEFT  -> {dfloor = floor;   droom = room-1;}
            case RIGHT -> {dfloor = floor;   droom = room+1;}
            default -> throw new IllegalArgumentException("Unexpected value: " + direction);
        }
        if (!building.isOccupied(dfloor, droom)) { // If destination is occupied, do nothing
            building.move(floor, room, direction, id);
            floor = dfloor; room = droom;
            if (floor == 0) {
                System.out.printf("About to return: " + this + "\n");
                mailroom.robotReturn(this);
            }
        }
    }

    void transfer(Robot robot) {  // Transfers every item assuming receiving robot has capacity
        ListIterator<Deliverable> iter = robot.deliverables.listIterator();
        while(iter.hasNext()) {
            Deliverable deliverable = iter.next();
            this.add(deliverable);
            usedCapacity -= deliverable.myWeight();//Hand it over
            iter.remove();
        }
    }

    void tick() {
            Building building = Building.getBuilding();
            if (deliverables.isEmpty()) {
                // Return to MailRoom
                if (room == building.NUMROOMS + 1) { // in right end column
                    move(Building.Direction.DOWN);  //move towards mailroom
                } else {
                    move(Building.Direction.RIGHT); // move towards right end column
                }
            } else {
                // Items to deliver
                if (floor == deliverables.getFirst().myFloor()) {
                    // On the right floor
                    if (room == deliverables.getFirst().myRoom()) { //then deliver all relevant items to that room
                        do {
                            Deliverable item_to_deliver = deliverables.removeFirst();
                            usedCapacity -= item_to_deliver.myWeight();
                            Simulation.deliver(item_to_deliver);
                        } while (!deliverables.isEmpty() && room == deliverables.getFirst().myRoom());
                    } else {
                        move(Building.Direction.RIGHT); // move towards next delivery
                    }
                } else {
                    move(Building.Direction.UP); // move towards floor
                }
            }
    }

    public String getId() {
        return id;
    }

    public int getCapacity(){ return capacity;}

    public int getUsedCapacity(){ return usedCapacity;}

    public int numItems () {
        return deliverables.size();
    }

    public void add(Deliverable item) {
        usedCapacity += item.myWeight();
        deliverables.add(item);
    }

    void sort() {
        Collections.sort(deliverables);
    }

}
