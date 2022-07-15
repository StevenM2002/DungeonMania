package dungeonmania;

import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.util.Position;

public class PlayerDataArgs {
    // These args are so that the PublisherManager class can extend the arguments and types of events passed without
    // causing other classes to need to update their arguments received from publisherListener update() interface method
    private Position previousPlayerPosition = null;
    private Potion potion = null;

    public Position getPreviousPlayerPosition() {
        return previousPlayerPosition;
    }

    public void setPreviousPlayerPosition(Position previousPlayerPosition) {
        this.previousPlayerPosition = previousPlayerPosition;
    }

    public Potion getPotion() {
        return potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }
}
