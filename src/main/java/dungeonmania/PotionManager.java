package dungeonmania;

import dungeonmania.CollectibleEntities.Potion;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PotionManager {
    private Queue<Potion> potionQueue = new LinkedBlockingQueue<>();
    public void addPotionEffect(int duration, Potion potion) {
        for (int i = 0; i < duration; i++) {
            potionQueue.add(potion);
        }
    }

    /**
     * Gets the next queued potion effect
     * @return null if there are no potion effects else the potion
     */
    public Potion getNextEffect() {
        if (potionQueue.isEmpty()) return null;
        return potionQueue.remove();
    }
}
