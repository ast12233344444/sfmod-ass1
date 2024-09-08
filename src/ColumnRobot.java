// [Mon 15:15] Team 03
public class ColumnRobot extends Robot{

    boolean isLeftRobot;
    public state RobotState;

    public ColumnRobot(MailRoom mr, int givenCapacity, boolean isLeft){
        super(mr, givenCapacity);
        isLeftRobot = isLeft;
    }

    @Override
    public void tick(){
        if (deliverables.isEmpty()) {
            move(Building.Direction.DOWN);  //move towards mailroom
            RobotState = state.MOVING_GENERAL;
        } else {
            if(floor < deliverables.get(0).myFloor()){
                move(Building.Direction.UP);
                if(floor < deliverables.get(0).myFloor())
                    RobotState = state.MOVING_GENERAL;
                else RobotState = state.WAITING;
            }
            else if(floor == deliverables.get(0).myFloor()){
                RobotState = state.WAITING;
            }

        }
    }
}
