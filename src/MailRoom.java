import java.util.*;

// [Mon 15:15] Team 03
public abstract class MailRoom {
    public enum Mode {CYCLING, FLOORING}
    protected List<Deliverable>[] waitingForDelivery;
    protected int numRobots;


    protected List<Robot> activeRobots;

    public boolean someItems() {
        for (int i = 0; i < Building.getBuilding().NUMFLOORS; i++) {
            if (!waitingForDelivery[i].isEmpty()) {
                    return true;
            }
        }
        return false;
    }

    protected int floorWithEarliestItem() {
        int floor = -1;
        int earliest = Simulation.now() + 1;
        for (int i = 0; i < Building.getBuilding().NUMFLOORS; i++) {
            if (!waitingForDelivery[i].isEmpty()) {
                int arrival = waitingForDelivery[i].getFirst().myArrival();
                if (earliest > arrival) {
                    floor = i;
                    earliest = arrival;
                }
            }
        }
        return floor;
    }

    protected MailRoom(int numFloors) {
        waitingForDelivery = new List[numFloors];
        for (int i = 0; i < numFloors; i++) {
            waitingForDelivery[i] = new LinkedList<>();
        }
    }

    public void arrive(List<Deliverable> items) {
        for (Deliverable item : items) {
            waitingForDelivery[item.myFloor()-1].add(item);
            System.out.printf("Item: Time = %d Floor = %d Room = %d Weight = %d\n",
                    item.myArrival(), item.myFloor(), item.myRoom(), item.myWeight());
        }
    }

    public abstract void tick();

    protected abstract void robotDispatch();

    public abstract void robotReturn(Robot robot);

    protected void loadRobot(int floor, Robot robot) {
        ListIterator<Deliverable> iter = waitingForDelivery[floor].listIterator();
        while (iter.hasNext()) {  // In timestamp order
            Deliverable deliverable = iter.next();
            if(robot.getCapacity() >= robot.getUsedCapacity() + deliverable.myWeight()) {
                robot.add(deliverable); //Hand it over
                iter.remove();
            }
        }
    }

}
