package dungeonmania.response.models;

import java.util.List;

public class RoundResponse {
    private double deltaPlayerHealth;
    private double deltaEnemyHealth;
    private List<ItemResponse> weaponryUsed;

    public RoundResponse(double deltaPlayerHealth, double deltaEnemyHealth, List<ItemResponse> weaponryUsed)
    {
        this.deltaPlayerHealth = deltaPlayerHealth;
        this.deltaEnemyHealth = deltaEnemyHealth;
        this.weaponryUsed = weaponryUsed;
    }

    public double getDeltaCharacterHealth(){
        return deltaPlayerHealth;
    }
    
    public double getDeltaEnemyHealth(){
        return deltaEnemyHealth;
    }

    public List<ItemResponse> getWeaponryUsed() { return weaponryUsed; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;

        RoundResponse roundResponse = (RoundResponse) obj;
        boolean weaponryEqual = true;
        for (int i = 0; i < weaponryUsed.size() && i < roundResponse.weaponryUsed.size(); i++) {
            if (!weaponryUsed.get(i).equals(roundResponse.weaponryUsed.get(i))) {
                weaponryEqual = false;
            }
        }
        return (roundResponse.deltaEnemyHealth == deltaEnemyHealth)
            && (roundResponse.deltaPlayerHealth == deltaPlayerHealth)
            && weaponryEqual;
    }
}
