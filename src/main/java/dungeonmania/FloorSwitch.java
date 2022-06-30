package dungeonmania;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntities {
    private boolean activated = false;

    public FloorSwitch(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    public boolean isActivated() {
        return activated;
    }

    public void activate(Entities entity) {

    }
}
