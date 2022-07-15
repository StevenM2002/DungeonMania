package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.CollectibleEntity;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.CollectibleEntities.Sword;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

public class Battle {
    private static List<BattleResponse> battleList = new ArrayList<>();

    public static List<RoundResponse> doRounds(Player player, MovingEntity enemy, List<CollectibleEntity> inventory) {
        List<RoundResponse> rounds = new ArrayList<>();
        List<ItemResponse> weaponsUsed = new ArrayList<>();

        double bow_mod = 1;
        double sword_mod = 0;
        double shield_mod = 0;
        int bow_usage = 0;

        if (getBow(inventory) != null) {
            bow_mod = getBow(inventory).getModifier();
            bow_usage = 1;
            weaponsUsed.add(new ItemResponse(getBow(inventory).getId(), "bow"));
            getBow(inventory).deteriorate();
        }

        if (getSword(inventory) != null) {
            sword_mod = getSword(inventory).getModifier();
            weaponsUsed.add(new ItemResponse(getSword(inventory).getId(), "sword"));
            getSword(inventory).deteriorate();
        }

        if (getShield(inventory) != null) {
            shield_mod = getSword(inventory).getModifier();
            weaponsUsed.add(new ItemResponse(getShield(inventory).getId(), "shield"));
            getShield(inventory).deteriorate();
        }

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            if (bow_usage == 1) {
                bow_mod = 2;
            }

            double deltaPlayerHealth = player.getHealth() - ((enemy.getAttack() - shield_mod) / 10);
            double deltaEnemyHealth = enemy.getHealth() - ((bow_mod * (player.getAttack() + sword_mod)) / 5);

            player.setHealth(deltaPlayerHealth);
            enemy.setHealth(deltaEnemyHealth);

            rounds.add(new RoundResponse(deltaPlayerHealth, deltaEnemyHealth, weaponsUsed));
            weaponsUsed.removeIf((ItemResponse i) -> i.getType().equals("bow"));
            bow_usage = 0;
        }

        return rounds;
    }

    public static Bow getBow(List<CollectibleEntity> inventory) {
        if (inventory.stream().anyMatch(e -> e.getCollectible() instanceof Bow)) {
            return ((Bow) inventory.stream()
                    .filter((CollectibleEntity e) -> e.getCollectible() instanceof Bow)
                    .collect(Collectors.toList()).get(0).getCollectible());
        }

        return null;
    }

    public static Sword getSword(List<CollectibleEntity> inventory) {
        if (inventory.stream().anyMatch(e -> e.getCollectible() instanceof Sword)) {
            return ((Sword) inventory.stream()
                    .filter((CollectibleEntity e) -> e.getCollectible() instanceof Sword)
                    .collect(Collectors.toList()).get(0).getCollectible());
        }

        return null;
    }

    public static Shield getShield(List<CollectibleEntity> inventory) {
        if (inventory.stream().anyMatch(e -> e.getCollectible() instanceof Shield)) {
            return ((Shield) inventory.stream()
                    .filter((CollectibleEntity e) -> e.getCollectible() instanceof Shield)
                    .collect(Collectors.toList()).get(0).getCollectible());
        }

        return null;
    }

    public static void doBattle(List<Entity> allEntities, List<CollectibleEntity> inventory) {
        Player player = (Player) allEntities.stream().filter(e -> e instanceof Player)
                .collect(Collectors.toList()).get(0);

        if (!allEntities.stream()
                .anyMatch(e -> e instanceof MovingEntity && e.getPosition().equals(player.getPosition()))) {
            return;
        }

        List<MovingEntity> allEnemies = allEntities.stream().filter((Entity e) -> e instanceof MovingEntity)
                .map(e -> (MovingEntity) e).collect(Collectors.toList());

        for (MovingEntity enemy : allEnemies) {
            double initialEnemyHealth = enemy.getHealth();
            double initialPlayerHealth = player.getHealth();

            List<RoundResponse> rounds = doRounds(player, enemy, inventory);
            battleList.add(
                    new BattleResponse(enemy.getClass().toString(), rounds, initialPlayerHealth, initialEnemyHealth));

            if (enemy.getHealth() <= 0) {
                allEntities.remove(enemy);

            } else if (player.getHealth() <= 0) {
                allEntities.removeIf((Entity e) -> e instanceof Player);
                return;
            }

        }

    }

    public static List<BattleResponse> getBattleList() {
        return battleList;
    }
}
