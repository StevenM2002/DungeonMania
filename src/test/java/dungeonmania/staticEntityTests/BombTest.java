package dungeonmania.staticEntityTests;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class BombTest {
    @Test
    public void explodeAllEntities() {
        var dmc = new DungeonManiaController();
        dmc.newGame("d_8_m2", "c_8_radius3_m2");
        dmc.tick(Direction.RIGHT);
        var invRes = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.tick(invRes.getInventory().stream().filter(itemResponse -> itemResponse.getType().equalsIgnoreCase("bomb")).map(ItemResponse::getId).findFirst().orElse(null)));
        assertEquals(1, dmc.getDungeonResponseModel().getEntities().size());
        assertEquals("player", dmc.getDungeonResponseModel().getEntities().get(0).getType().toLowerCase());
    }

    @Test
    public void explodeSome() {
        var dmc = new DungeonManiaController();
        dmc.newGame("d_8_m2", "c_8_radius1_m2");
        dmc.tick(Direction.RIGHT);
        var invRes = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.tick(invRes.getInventory().stream().filter(itemResponse -> itemResponse.getType().equalsIgnoreCase("bomb")).map(ItemResponse::getId).findFirst().orElse(null)));
        assertEquals(8, dmc.getDungeonResponseModel().getEntities().size());
        assertEquals(1, dmc.getDungeonResponseModel().getEntities().stream().filter(it -> it.getType().equalsIgnoreCase("assassin")).count());
        assertEquals(6, dmc.getDungeonResponseModel().getEntities().stream().filter(it -> it.getType().equalsIgnoreCase("wall")).count());
    }

}
