import java.util.*;

import static java.lang.String.format;

public class CyclingMailRoom extends MailRoom{

    private Queue<Robot> idleRobots;
    private List<Robot> deactivatingRobots; // Don't treat a robot as both active and idle by swapping directly

    public CyclingMailRoom(int numFloors, int numRobots, int maxRobotCapacity){
            super(numFloors);
            this.numRobots = numRobots;

            idleRobots = new LinkedList<>();
            for (int i = 0; i < numRobots; i++)
                idleRobots.add(new CyclingRobot(this, maxRobotCapacity));  // In mailroom, floor/room is not significant
            activeRobots = new ArrayList<>();
            deactivatingRobots = new ArrayList<>();
        }

    @Override
    protected void robotDispatch() { // Can dispatch at most one robot; it needs to move out of the way for the next
        System.out.println("Dispatch at time = " + Simulation.now());
        // Need an idle robot and space to dispatch (could be a traffic jam)

        if (!idleRobots.isEmpty() && !Building.getBuilding().isOccupied(0,0)) {
            int fwei = floorWithEarliestItem();
            if (fwei >= 0) {  // Need an item or items to deliver, starting with earliest
                Robot robot = idleRobots.remove();
                loadRobot(fwei, robot);
                // Room order for left to right delivery
                robot.sort();
                activeRobots.add(robot);
                System.out.println("Dispatch @ " + Simulation.now() +
                        " of Robot " + robot.getId() + " with " + robot.numItems() + " item(s)");
                robot.place(0, 0);
            }
        }
    }

    @Override
    public void robotReturn(Robot robot) {
        Building building = Building.getBuilding();
        int floor = robot.getFloor();
        int room = robot.getRoom();
        assert floor == 0 && room == building.NUMROOMS+1: format("robot returning from wrong place - floor=%d, room ==%d", floor, room);
        assert robot.isEmpty() : "robot has returned still carrying at least one item";
        building.remove(floor, room);
        deactivatingRobots.add(robot);
    }

    @Override
    public void tick() { // Simulation time unit
        for (Robot activeRobot : activeRobots) {
            System.out.printf("About to tick: " + activeRobot.toString() + "\n"); activeRobot.tick();
        }
        robotDispatch();  // dispatch a robot if conditions are met
        // These are returning robots who shouldn't be dispatched in the previous step
        ListIterator<Robot> iter = deactivatingRobots.listIterator();
        while (iter.hasNext()) {  // In timestamp order
            Robot robot = iter.next();
            iter.remove();
            activeRobots.remove(robot);
            idleRobots.add(robot);
        }
    }
}

