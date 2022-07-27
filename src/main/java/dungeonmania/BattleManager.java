package dungeonmania;

import java.util.ArrayList;
import java.util.List;

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
}
