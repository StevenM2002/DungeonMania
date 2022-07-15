package dungeonmania.StaticEntities;

public interface Switch {
    /**
     * returns whether the switch is activated or not
     * @return
     */
    public boolean getActivated();

    /**
     * sets the switch to activated or not
     * @param activated
     */
    public void setActivated(boolean activated);

    /**
     * the type of entity that activates this switch
     * @return
     */
    public String getActivationType();
}
