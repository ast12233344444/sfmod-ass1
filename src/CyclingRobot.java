// [Mon 15:15] Team 03
public class CyclingRobot extends Robot {

    public CyclingRobot(MailRoom mr, int givenCapacity){
        super(mr, givenCapacity);
    }
    @Override
    public void tick() {
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
}
