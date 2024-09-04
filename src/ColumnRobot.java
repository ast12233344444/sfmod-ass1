public class ColumnRobot extends Robot{

    boolean isLeftRobot;
    public state RobotState;

    public ColumnRobot(MailRoom mr, int given_capacity, boolean is_left){
        super(mr, given_capacity);
        isLeftRobot = is_left;
    }

    @Override
    void tick(){
        Building building = Building.getBuilding();
        if (deliverables.isEmpty()) {
            move(Building.Direction.DOWN);//move towards mailroom
            RobotState = state.MOVING_GENERAL;
        } else {
            if(floor < deliverables.get(0).myFloor()){
                move(Building.Direction.UP);
                //RobotState = state.MOVING_GENERAL;
                if(floor < deliverables.get(0).myFloor())RobotState = state.MOVING_GENERAL;
                else RobotState = state.WAITING;
            }
            else if(floor == deliverables.get(0).myFloor()){
                RobotState = state.WAITING;
                /*if(isLeftRobot){
                    if(building.isOccupied(floor, 1) && mailroom.activeRobots.get(floor-1).RobotState == state.WAITING){
                        transfer(mailroom.activeRobots.get(floor-1));
                    }
                }else{
                    if(building.isOccupied(floor, building.NUMROOMS) && mailroom.activeRobots.get(floor-1).RobotState == state.WAITING){
                        transfer(mailroom.activeRobots.get(floor-1));
                    }
                }*/
            }

        }
    }
}
