// [Mon 15:15] Team 03
public class RowRobot extends Robot{
    public RowRobot(MailRoom mr, int givenCapacity){
        super(mr, givenCapacity);
        RobotState = state.WAITING;
    }

    @Override
    public void tick(){
        Building building = Building.getBuilding();
        boolean isempty_begin = false;
        if (deliverables.isEmpty()) {
            isempty_begin = true;
            if(building.isOccupied(floor, 0)
                    && (RobotState != state.MOVING_RIGHT_ROW)&&
                    ((((FlooringMailRoom)mailroom).leftColRobot.RobotState == state.WAITING))){
                if(room == 1){
                    RobotState = state.WAITING;
                    transfer(((FlooringMailRoom)mailroom).leftColRobot);
                }else{
                    RobotState = state.MOVING_LEFT_ROW;
                    move(Building.Direction.LEFT);
                }
            }else if(building.isOccupied(floor, building.NUMROOMS+1)
                    && (RobotState != state.MOVING_LEFT_ROW)
                    && (((FlooringMailRoom)mailroom).rightColRobot.RobotState == state.WAITING)){
                if(room == building.NUMROOMS){
                    RobotState = state.WAITING;
                    transfer(((FlooringMailRoom)mailroom).rightColRobot);
                }else{
                    RobotState = state.MOVING_RIGHT_ROW;
                    move(Building.Direction.RIGHT);
                }
            }else{
                RobotState = state.WAITING;
            }
        }else if(RobotState == state.WAITING){
            if(room == 1){
                sort();
            }if(room == building.NUMROOMS){
                reverseSort();
            }
            RobotState = state.MOVING_GENERAL;
        }

        if(!isempty_begin){
            if (room == deliverables.getFirst().myRoom()) {
                do {
                    Deliverable item_to_deliver = deliverables.removeFirst();
                    usedCapacity -= item_to_deliver.myWeight();
                    Simulation.deliver(item_to_deliver);
                } while (!deliverables.isEmpty() && room == deliverables.getFirst().myRoom());
            }else{
                if(room < deliverables.getFirst().myRoom())move(Building.Direction.RIGHT);
                if(room > deliverables.getFirst().myRoom())move(Building.Direction.LEFT);
            }
        }
    }
}
