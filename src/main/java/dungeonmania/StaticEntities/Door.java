package dungeonmania.StaticEntities;

import dungeonmania.CollectibleEntities.Key;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int key;

    public Door(String id, Position position, int key) {
        super(id, position, false);
        this.key = key;
    }

    public boolean keyMatchesDoor(Key k) {
        return k.getKey() == key;
    }

}