package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    public Spider(String id, Position position, double health, double attack) {
        super(id, position, false, health, attack, new CircularMovement());
    }

}
