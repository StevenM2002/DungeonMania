package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.MidnightArmour;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.CollectibleEntities.Sword;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

import static dungeonmania.DungeonManiaController.getDmc;


public class BattleManager {
    private List<BattleResponse> battleList;
    public int getVictimCount() {
        return battleList.size();
    }

    public BattleManager() {
        this.battleList = new ArrayList<>();
    }
    /**
     * constructs the battle manager from a jsonArray containing a previously
     * used battleList
     * @param battleListJSON
     */
    public BattleManager(JSONArray battleListJSON) {
        this.battleList = new ArrayList<>();
        for (int i = 0; i < battleListJSON.length(); i++) {
            JSONObject battle = battleListJSON.getJSONObject(i);
            List<RoundResponse> rounds = new ArrayList<>();
            for (int j = 0; j < battle.getJSONArray("rounds").length(); j++) {
                JSONObject round = battle.getJSONArray("rounds").getJSONObject(j);
                List<ItemResponse> weaponryUsed = new ArrayList<>();
                for (int l = 0; l < round.getJSONArray("weaponryUsed").length(); l++) {
                    JSONObject weapon = round.getJSONArray("weaponryUsed").getJSONObject(l);
                    weaponryUsed.add(new ItemResponse(weapon.getString("id"), weapon.getString("type")));
                }
                rounds.add(new RoundResponse(round.getDouble("deltaPlayerHealth"), round.getDouble("deltaEnemyHealth"), weaponryUsed));
            }
            battleList.add(new BattleResponse(battle.getString("enemy"), rounds, battle.getDouble("initialPlayerHealth"), battle.getDouble("initialEnemyHealth")));
        }
    }


    private static boolean hasBow(List<InventoryObject> inventory) {
        return inventory.stream().anyMatch(e -> e instanceof Bow);
    }

    private static boolean hasSword(List<InventoryObject> inventory) {
        return inventory.stream().anyMatch(e -> e instanceof Sword);
    }

    private static boolean hasShield(List<InventoryObject> inventory) {
        return inventory.stream().anyMatch(e -> e instanceof Shield);
    }

    private static boolean hasMidnightArmour(List<InventoryObject> inventory) {
        return inventory.stream().anyMatch(e -> e instanceof MidnightArmour);
    }
    
    public List<RoundResponse> doRounds(Player player, MovingEntity enemy) {
        List<RoundResponse> rounds = new ArrayList<>();
        List<InventoryObject> inventory = player.getInventory();
        List<ItemResponse> weaponsUsed = new ArrayList<>();


        // getting and deteriorating the item.
        if (hasBow(inventory)) {
            weaponsUsed.add(new ItemResponse(((Bow) inventory.stream().filter(e -> e instanceof Bow).findFirst().get()).getId(), "bow"));
        }

        if (hasSword(inventory)) {
            weaponsUsed.add(new ItemResponse(((Sword) inventory.stream().filter(e -> e instanceof Sword).findFirst().get()).getId(), "sword"));
        }

        if (hasShield(inventory)) {
            weaponsUsed.add(new ItemResponse(((Shield) inventory.stream().filter(e -> e instanceof Shield).findFirst().get()).getId(), "shield"));
        }

        if (hasMidnightArmour(inventory)) {
            weaponsUsed.add(new ItemResponse(((MidnightArmour) inventory.stream().filter(e -> e instanceof MidnightArmour).findFirst().get()).getId(), "midnight_armour"));
        }

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            double deltaPlayerHealth = player.takeDamage(enemy.dealDamage());
            double deltaEnemyHealth = enemy.takeDamage(player.dealDamage());
            rounds.add(new RoundResponse(-deltaPlayerHealth, -deltaEnemyHealth, weaponsUsed));
        }

        if (weaponsUsed.stream().anyMatch(item -> item.getType().equals("bow"))) {
            Bow bow = (Bow) inventory.stream().filter(e -> e instanceof Bow).findFirst().get();
            if (bow.deteriorate()) inventory.remove(bow);
        }

        if (weaponsUsed.stream().anyMatch(item -> item.getType().equals("sword"))) {
            Sword sword = (Sword) inventory.stream().filter(e -> e instanceof Sword).findFirst().get();
            if (sword.deteriorate()) inventory.remove(sword);
        }

        if (weaponsUsed.stream().anyMatch(item -> item.getType().equals("shield"))) {
            Shield shield = (Shield) inventory.stream().filter(e -> e instanceof Shield).findFirst().get();
            if (shield.deteriorate()) inventory.remove(shield);
        }

        return rounds;
    }

    public void doBattle(Player player, MovingEntity enemy) {
        double initialEnemyHealth = enemy.getHealth();
        double initialPlayerHealth = player.getHealth();
        List<RoundResponse> rounds = doRounds(player, enemy);
        battleList.add(
            new BattleResponse(
                enemy.getClass().toString(), 
                rounds, 
                initialPlayerHealth, 
                initialEnemyHealth
            )
        );

        if (enemy.getHealth() <= 0) {
            getDmc().getAllEntities().remove(enemy);

        } else if (player.getHealth() <= 0) {
            getDmc().getAllEntities().remove(player);
        }
    }

    public List<BattleResponse> getBattleList() {
        return battleList;
    }

    public JSONArray battleListToJSON() {
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
