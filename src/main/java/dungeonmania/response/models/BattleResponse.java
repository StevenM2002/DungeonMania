package dungeonmania.response.models;

import java.util.ArrayList;
import java.util.List;

public final class BattleResponse {
    private final String enemy;
    private final double initialPlayerHealth;
    private final double initialEnemyHealth;
    private final List<RoundResponse> rounds;
    
    public BattleResponse(){
        this.initialPlayerHealth = 0;
        this.initialEnemyHealth = 0;
        this.enemy = "";
        this.rounds = new ArrayList<RoundResponse>();
    }

    public BattleResponse(String enemy, List<RoundResponse> rounds, double initialPlayerHealth, double initialEnemyHealth) {
        this.initialPlayerHealth = initialPlayerHealth;
        this.initialEnemyHealth = initialEnemyHealth;
        this.enemy = enemy;
        this.rounds = rounds;
    }

    public final String getEnemy(){
        return enemy;
    }

    public final double getInitialPlayerHealth(){
        return initialPlayerHealth;
    }

    public final double getInitialEnemyHealth(){
        return initialEnemyHealth;
    }

    public final List<RoundResponse> getRounds(){
        return rounds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;

        BattleResponse battleResponse = (BattleResponse) obj;
        boolean roundsEqual = true;
        for (int i = 0; i < rounds.size() && i < battleResponse.rounds.size(); i++) {
            if (!rounds.get(i).equals(battleResponse.rounds.get(i))) {
                roundsEqual = false;
            }
        }
        return (battleResponse.initialEnemyHealth == initialEnemyHealth)
            && (battleResponse.initialPlayerHealth == initialPlayerHealth)
            && (enemy.equals(battleResponse.enemy))
            && roundsEqual;
    }
}
