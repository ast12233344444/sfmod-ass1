import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public abstract class Robot {
    protected static int count = 1;
    protected static int capacity;
    final private String id;
    protected int floor;
    protected int room;
    protected int usedCapacity;
    final protected MailRoom mailroom;
    final protected List<Deliverable> deliverables = new ArrayList<>();
    public state RobotState;


    public enum state{
        MOVING_GENERAL,
        WAITING,
        MOVING_RIGHT_ROW,
        MOVING_LEFT_ROW
    }

    public String toString() {
        return "Id: " + id + " Floor: " + floor + ", Room: " + room + ", #items: " + numItems() + ", Load: " + usedCapacity ;
    }

    Robot(MailRoom mailroom, int given_capacity) {
        this.id = "R" + count++;
        this.mailroom = mailroom;
        capacity = given_capacity;
        usedCapacity = 0;
        RobotState = state.MOVING_GENERAL;
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

    protected void move(Building.Direction direction) {
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
            add(deliverable);
            robot.usedCapacity -= deliverable.myWeight();//Hand it over
            iter.remove();
        }
    }

    public abstract void tick();

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

    void sort(){
        Collections.sort(deliverables);
    }

    void reversesort(){
        Collections.sort(deliverables, Collections.reverseOrder());
    }
}