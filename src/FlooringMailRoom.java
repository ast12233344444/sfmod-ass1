import java.util.ArrayList;
import static java.lang.String.format;

// [Mon 15:15] Team 03
public class FlooringMailRoom extends MailRoom{
    ColumnRobot leftColRobot;
    ColumnRobot rightColRobot;
    private boolean isLeftRobotActive;
    private boolean isRightRobotActive;
    public FlooringMailRoom(int numFloors, int maxRobotCapacity){
        super(numFloors);
        activeRobots = new ArrayList<>();

        this.numRobots = numFloors+2;
        leftColRobot = new ColumnRobot(this, maxRobotCapacity, true);
        rightColRobot = new ColumnRobot(this, maxRobotCapacity, false);
        for(int i =0; i < numFloors; i++){
            Robot r =  new RowRobot(this, maxRobotCapacity);
            r.place(i+1, 1);
            activeRobots.add(r);
        }
        isLeftRobotActive = false;
        isRightRobotActive = false;
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
            isLeftRobotActive = false;
        }
        if(room == building.NUMROOMS+1){
            isRightRobotActive = false;
        }
    }

    @Override
    protected void robotDispatch() {

        ColumnRobot targetRobot =  null;
        if(!isLeftRobotActive) targetRobot = leftColRobot;
        else if(!isRightRobotActive) targetRobot = rightColRobot;

        if(!isLeftRobotActive){
            sendRobot(leftColRobot);
        }else leftColRobot.tick();

        if(!isRightRobotActive){
            sendRobot(rightColRobot);
        }else rightColRobot.tick();
    }

    private void sendRobot(ColumnRobot targetRobot){
        Building building = Building.getBuilding();
        int fwei = floorWithEarliestItem();
        if (fwei >= 0) {  // Need an item or items to deliver, starting with earliest
            loadRobot(fwei, targetRobot);

            System.out.println("Dispatch at time = " + Simulation.now());
            System.out.println("Dispatch @ " + Simulation.now() +
                    " of Robot " + targetRobot.getId() + " with " + targetRobot.numItems() + " item(s)");

            if(targetRobot.isLeftRobot){
                targetRobot.place(0, 0);
                isLeftRobotActive = true;
            }
            else {
                targetRobot.place(0, building.NUMROOMS+1);
                isRightRobotActive = true;
            }
        }
    }

    @Override
    public void tick() { // Simulation time unit

        for (Robot activeRobot : activeRobots) {
            System.out.printf("About to tick: " + activeRobot.toString() + "\n");
            activeRobot.tick();
        }

        robotDispatch();  // dispatch a robot if conditions are met
    }
}
