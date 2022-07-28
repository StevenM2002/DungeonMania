package dungeonmania.CollectibleEntities;


import dungeonmania.Entity;
import dungeonmania.util.Position;

import java.util.List;

import static dungeonmania.DungeonManiaController.getDmc;

public class Bomb extends InventoryObject implements MapCollectible {
    private static int radius;
    public Bomb(String id, int radius) {
        super(id);
        this.radius = radius;
    }

}
