package dungeonmania.StaticEntities;

import dungeonmania.CollectibleEntities.Key;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int key;
    private boolean locked;
    public Door(String id, Position position, int key) {
        super(id, position, false);
        this.key = key;
        this.locked = true;
    }

    public boolean keyMatchesDoor(Key k) {
        return k.getKey() == key;
    }

    public void unlock() {
        locked = false;
    }
    public boolean isLocked() {
        return locked;
    }

    @Override
    public String getDefaultCollision() {
        if (locked) {
            return "Block";
        } 
        return "Pass";
    }
}