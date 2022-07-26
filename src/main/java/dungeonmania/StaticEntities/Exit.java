package dungeonmania.StaticEntities;

import org.json.JSONObject;

import dungeonmania.util.Position;

public class Exit extends StaticEntity  implements Switch{
    private boolean activated = false;
    public Exit(String id, Position position) {
        super(id, position, false);
    }
    @Override
    public boolean getActivated() {
        return activated;
    }
    @Override
    public void setActivated(boolean activated) {
        this.activated = activated;        
    }
    @Override
    public String getActivationType() {
        return "Player";
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("activated", activated);
        return newJSON;
    }
    
    
}