package dungeonmania.CollectibleEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class CollectibleEntity extends Entity implements MapCollectible {
    private MapCollectible collectible;

    public MapCollectible getCollectible() {
        return collectible;
    }

    public CollectibleEntity(String id, Position position, MapCollectible collectible) {
        super(id, position, false);
        this.collectible = collectible;
    }
}
