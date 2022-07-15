package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements Switch {
    private boolean activated = false;

    public FloorSwitch(String id, Position position) {
        super(id, position, false);
    }

    @Override
    public boolean getActivated() {
        return activated;
    }

    @Override
    public void setActivated(boolean activated) {
        this.activated = activated;        
    }

    @Override
    public String getActivationType() {
        return "Boulder";
    }


}
