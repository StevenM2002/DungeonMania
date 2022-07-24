package dungeonmania.MovingEntities;


public interface Battling {

    /**
     * returns the amount of damage the entity took.
     * adjusts the entities health accordingly
     * @param damage
     * @return
     */
    public double takeDamage(double damage);

    /**
     * returns the amount of damage the entity would deal
     * @return
     */
    public double dealDamage();
}
