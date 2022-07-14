package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Exit extends StaticEntity {
    // TODO: make a switch interface to make them switch on and off
    private boolean isActivated = false;
    public Exit(String id, Position position) {
        super(id, position, false);
    }
}