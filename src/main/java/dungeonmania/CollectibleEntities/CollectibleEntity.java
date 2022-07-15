package dungeonmania.CollectibleEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class CollectibleEntity extends Entity implements MapCollectible {
    private MapCollectible collectible;

    public MapCollectible getCollectible() {
        return collectible;
    }

    @Override
    public String getType() {
        return collectible.getClass().getSimpleName();
    }

    public CollectibleEntity(String id, Position position, MapCollectible collectible) {
        super(id, position, false);
        this.collectible = collectible;
    }
}
