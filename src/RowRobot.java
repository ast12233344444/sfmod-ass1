public class RowRobot extends Robot{


    public RowRobot(MailRoom mr, int given_capacity){
        super(mr, given_capacity);
        RobotState = state.WAITING;
    }

    @Override
    void tick(){//TODO execute the floor robots before column robots robots
        Building building = Building.getBuilding();
        if (deliverables.isEmpty()) {
            if(building.isOccupied(floor, 0)
                    && (RobotState != state.MOVING_RIGHT_ROW)&&
                    ((((FlooringMailRoom)mailroom).leftcolrobot.RobotState == state.WAITING))){
                if(room == 1){
                    RobotState = state.WAITING;
                }else{
                    RobotState = state.MOVING_LEFT_ROW;
                    move(Building.Direction.LEFT);
                }
            }else if(building.isOccupied(floor, building.NUMROOMS+1)
                    && (RobotState != state.MOVING_LEFT_ROW)
                    && (((FlooringMailRoom)mailroom).rightcolrobot.RobotState == state.WAITING)){
                if(room == building.NUMROOMS){
                    RobotState = state.WAITING;
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
                reversesort();
            }
            RobotState = state.MOVING_GENERAL;
        }

        if(!deliverables.isEmpty()){
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
