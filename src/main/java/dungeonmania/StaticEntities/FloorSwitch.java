package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean activated = false;

    public FloorSwitch(String id, Position position) {
        super(id, position, false);
    }

    public boolean isActivated() {
        return activated;
    }

    public void activate(Entity entity) {
        activated = true;
    }
}
