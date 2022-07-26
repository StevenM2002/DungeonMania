package dungeonmania;

import dungeonmania.CollectibleEntities.Potion;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PotionManager {
    private static Potion currPotion = null;
    private static Queue<Potion> potionQueue = new LinkedBlockingQueue<>();
    
    public static void addPotionEffect(int duration, Potion potion) {
        for (int i = 0; i < duration; i++) {
            potionQueue.add(potion);
        }
    }
    public static Potion getCurrPotion() {
        return currPotion;
    }
    /**
     * Gets the next queued potion effect
     * @return null if there are no potion effects else the potion
     */
    public static Potion getNextEffect() {
        if (potionQueue.isEmpty()) {
            currPotion = null;
        } else {
            currPotion = potionQueue.remove();
        }
        return currPotion;
    }
    public static Queue<Potion> getPotionQueue() {
        return potionQueue;
    }
    public static void setCurrPotion(Potion currPotion) {
        PotionManager.currPotion = currPotion;
    }
    public static void setPotionQueue(Queue<Potion> potionQueue) {
        PotionManager.potionQueue = potionQueue;
    }
}
