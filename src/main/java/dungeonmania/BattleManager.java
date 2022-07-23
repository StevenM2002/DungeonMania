package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.MidnightArmor;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.CollectibleEntities.Sword;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

public class BattleManager {
    private List<BattleResponse> battleList;
    private DungeonManiaController dmc;
    public int getVictimCount() {
        return battleList.size();
    }

    public BattleManager(DungeonManiaController dmc) {
        this.dmc = dmc;
        this.battleList = new ArrayList<>();
    }

    public List<RoundResponse> doRounds(Player player, MovingEntity enemy) {
        List<InventoryObject> inventory = player.getInventory();
        List<RoundResponse> rounds = new ArrayList<>();
        List<ItemResponse> weaponsUsed = new ArrayList<>();
        Bow bow = getBow(inventory);
        Sword sword = getSword(inventory);
        Shield shield = getShield(inventory);
        MidnightArmor midnightArmor = getMidnightArmor(inventory);

        double bowMod = 1;
        double swordMod = 0;
        double shieldMod = 0;
        double midnightArmourAttack = 0;
        double midnightArmourDefence = 0;


        // getting and deteriorating the items
        if (bow != null) {
            bowMod = bow.getModifier();
            weaponsUsed.add(new ItemResponse(bow.getId(), "bow"));
            if (bow.deteriorate()) {
                inventory.remove(bow);
            }
        }

        if (sword != null) {
            swordMod = sword.getModifier();
            weaponsUsed.add(new ItemResponse(sword.getId(), "sword"));
            if (sword.deteriorate()) {
                inventory.remove(sword);
            }
        }

        if (shield != null) {
            shieldMod = shield.getDefence();
            weaponsUsed.add(new ItemResponse(shield.getId(), "shield"));
            if (shield.deteriorate()) {
                inventory.remove(shield);
            }
        }

        if (midnightArmor != null) {
            midnightArmourAttack = midnightArmor.getModifier();
            midnightArmourDefence = midnightArmor.getDefence();
            weaponsUsed.add(new ItemResponse(midnightArmor.getId(), "midnight_armour"));
        }

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            double deltaPlayerHealth = (enemy.getAttack() - shieldMod - midnightArmourDefence) / 10;
            double deltaEnemyHealth = (bowMod * (player.getAttack() + swordMod + midnightArmourAttack)) / 5;

            player.setHealth(player.getHealth() - deltaPlayerHealth);
            enemy.setHealth(enemy.getHealth() - deltaEnemyHealth);

            rounds.add(new RoundResponse(-deltaPlayerHealth, -deltaEnemyHealth, weaponsUsed));
        }

        return rounds;
    }


    private static Bow getBow(List<InventoryObject> inventory) {
        if (inventory.stream().anyMatch(e -> e instanceof Bow)) {
            return ((Bow) inventory.stream()
                    .filter(e -> e instanceof Bow)
                    .findFirst().get());
        }

        return null;
    }

    private static Sword getSword(List<InventoryObject> inventory) {
        if (inventory.stream().anyMatch(e -> e instanceof Sword)) {
            return ((Sword) inventory.stream()
                    .filter(e -> e instanceof Sword)
                    .findFirst().get());
        }

        return null;
    }

    private static Shield getShield(List<InventoryObject> inventory) {
        if (inventory.stream().anyMatch(e -> e instanceof Shield)) {
            return ((Shield) inventory.stream()
                    .filter(e -> e instanceof Shield)
                    .findFirst().get());
        }

        return null;
    }

    private static MidnightArmor getMidnightArmor(List<InventoryObject> inventory) {
        if (inventory.stream().anyMatch(e -> e instanceof MidnightArmor)) {
            return ((MidnightArmor) inventory.stream()
                    .filter(e -> e instanceof MidnightArmor)
                    .findFirst().get());
        }

        return null;
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
            dmc.getAllEntities().remove(enemy);

        } else if (player.getHealth() <= 0) {
            dmc.getAllEntities().remove(player);
        }
    }

    public List<BattleResponse> getBattleList() {
        return battleList;
    }
}
