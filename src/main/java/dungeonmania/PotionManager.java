package dungeonmania;

import dungeonmania.CollectibleEntities.Potion;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PotionManager {
    private Potion currPotion = null;
    private Queue<Potion> potionQueue = new LinkedBlockingQueue<>();
    public void addPotionEffect(int duration, Potion potion) {
        for (int i = 0; i < duration; i++) {
            potionQueue.add(potion);
        }
    }
    public Potion getCurrPotion() {
        return currPotion;
    }
    /**
     * Gets the next queued potion effect
     * @return null if there are no potion effects else the potion
     */
    public Potion getNextEffect() {
        if (potionQueue.isEmpty()) {
            currPotion = null;
        } else {
            currPotion = potionQueue.remove();
        }
        return currPotion;
    }
}
