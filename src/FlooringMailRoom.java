import java.util.ArrayList;
import java.util.ListIterator;

import static java.lang.String.format;

public class FlooringMailRoom extends MailRoom{
    ColumnRobot leftcolrobot;
    ColumnRobot rightcolrobot;
    boolean isleftrobotactive;
    boolean isrightrobotactive;
    public FlooringMailRoom(int numFloors, int maxRobotCapacity){
        super(numFloors);
        activeRobots = new ArrayList<>();

        this.numRobots = numFloors+2;
        leftcolrobot = new ColumnRobot(this, maxRobotCapacity, true);
        rightcolrobot = new ColumnRobot(this, maxRobotCapacity, false);
        for(int i =0; i < numFloors; i++){
            Robot r =  new RowRobot(this, maxRobotCapacity);
            r.place(i+1, 1);
            activeRobots.add(r);
        }
        isleftrobotactive = false;
        isrightrobotactive = false;
    }

    @Override
    public void robotReturn(Robot robot) {
        Building building = Building.getBuilding();
        int floor = robot.getFloor();
        int room = robot.getRoom();
        assert floor == 0 && (room == building.NUMROOMS+1 || room == 0): format("robot returning from wrong place - floor=%d, room ==%d", floor, room);
        assert robot.isEmpty() : "robot has returned still carrying at least one item";
        building.remove(floor, room);
        if(room == 0){
            building.remove(0,0);
            isleftrobotactive = false;
        }
        if(room == building.NUMROOMS+1){
            building.remove(0, building.NUMROOMS);
            isrightrobotactive = false;
        }
    }

    @Override
    protected void robotDispatch() { // Can dispatch at most one robot; it needs to move out of the way for the next
        Building building = Building.getBuilding();
        // Need an idle robot and space to dispatch (could be a traffic jam)

        ColumnRobot targetrobot =  null;
        if(!isleftrobotactive)targetrobot = leftcolrobot;
        else if(!isrightrobotactive)targetrobot = rightcolrobot;

        if(targetrobot != null){
            int fwei = floorWithEarliestItem();
            if (fwei >= 0) {  // Need an item or items to deliver, starting with earliest
                loadRobot(fwei, targetrobot);

                System.out.println("Dispatch at time = " + Simulation.now());
                System.out.println("Dispatch @ " + Simulation.now() +
                        " of Robot " + targetrobot.getId() + " with " + targetrobot.numItems() + " item(s)");

                if(targetrobot.isLeftRobot){
                    targetrobot.place(0, 0);
                    isleftrobotactive = true;
                }
                else {
                    targetrobot.place(0, building.NUMROOMS+1);
                    isrightrobotactive = true;
                }
            }
        }
    }

    @Override
    public void tick() { // Simulation time unit
        for (Robot activeRobot : activeRobots) {
            System.out.printf("About to tick: " + activeRobot.toString() + "\n"); activeRobot.tick();
        }
        if(isrightrobotactive)rightcolrobot.tick();
        if(isleftrobotactive)leftcolrobot.tick();
        robotDispatch();  // dispatch a robot if conditions are met
        robotDispatch();  // can dispatch two robots possibly

    }
}
