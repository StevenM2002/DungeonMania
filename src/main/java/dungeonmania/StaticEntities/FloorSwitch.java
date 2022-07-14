package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean activated = false;

    public FloorSwitch(String id, Position position) {
        super(id, position, false);
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
