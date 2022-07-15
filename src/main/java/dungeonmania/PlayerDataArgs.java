package dungeonmania;

import dungeonmania.CollectibleEntities.Potion;

public class PlayerDataArgs {
    // These args are so that the PublisherManager class can extend the arguments and types of events passed without
    // causing other classes to need to update their arguments received from publisherListener update() interface method
    private Potion potion = null;

    public Potion getPotion() {
        return potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }
}
