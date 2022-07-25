package dungeonmania;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

public class DungeonSaver {
    private JSONObject savedDungeon;

    public DungeonSaver(JSONObject dungeon, JSONObject config, DungeonManiaController dmc, String name, String dungeonName, int dungeonID) {
        savedDungeon.put("name", name);
        savedDungeon.put("config", config);
        savedDungeon.put("goal-condition", dungeon.getJSONObject("goal-condition"));
        savedDungeon.put("dungeonName", dungeonName);
        savedDungeon.put("currentDungeonID", dungeonID);
        savedDungeon.put("ticks", new JSONArray());
    }

    /**
     * Stores all the information regarding the current tick in the savedDungeon
     * Stored information includes:
     * @apiNote the current tick
     * @apiNote the current entity id from EntityFactory
     * @apiNote the current id from CraftingManager
     * @apiNote the current potion in Potionmanager
     * @apiNote the potion queue
     * @apiNote the list of all entities converted to JSON. NOTE: the JSON object
     * representing the player stores the inventory
     * @apiNote stores the battleList, converted to JSON
     * @param dmc
     */
    public void storeCurrentTick(DungeonManiaController dmc) {
        JSONObject currTick = new JSONObject();
        currTick.put("tick", dmc.getCurrTick());
        currTick.put("currentEntityID", EntityFactory.getCurrentEntityID());
        currTick.put("currentCraftingID", CraftingManager.getIDCounter());
        
        // doing potions
        currTick.put("currPotion", PotionManager.getCurrPotion().getName());
        currTick.put("potionQueue", new JSONArray());
        PotionManager.getPotionQueue()
            .forEach(p->currTick.getJSONArray("potionQueue").put((new JSONObject()).put("potion", p.getName())));
        
        // doing entity list
        currTick.put("entities", new JSONArray());
        for (Entity e : dmc.getAllEntities()) {
            currTick.getJSONArray("entities").put(e.toJSON());
        }

        currTick.put("battleList", battleListToJSON(dmc.getBattleManager().getBattleList()));
        savedDungeon.getJSONArray("ticks").put(currTick);
        
    }

    private JSONArray battleListToJSON(List<BattleResponse> battleList) {
        JSONArray JSONBattleList = new JSONArray();
        for (BattleResponse b : battleList) {
            JSONObject battle = new JSONObject();
            battle.put("enemy", b.getEnemy());
            battle.put("initialPlayerHealth", b.getInitialPlayerHealth());
            battle.put("initialEnemyHealth", b.getInitialEnemyHealth());
            battle.put("rounds", new JSONArray());
            for (RoundResponse r : b.getRounds()) {
                JSONObject round = new JSONObject();
                round.put("deltaPlayerHealth", r.getDeltaCharacterHealth());
                round.put("deltaEnemyHealth", r.getDeltaEnemyHealth());
                round.put("weaponryUsed", new JSONArray());
                for (ItemResponse i : r.getWeaponryUsed()) {
                    JSONObject weapon = new JSONObject();
                    weapon.put("id", i.getId());
                    weapon.put("type", i.getType());
                    round.getJSONArray("weaponryUsed").put(weapon);
                }
                battle.getJSONArray("rounds").put(round);
            }
            JSONBattleList.put(battle);
        }
        return JSONBattleList;
    } 
    
}
