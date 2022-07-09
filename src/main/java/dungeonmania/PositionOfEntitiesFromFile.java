package dungeonmania;

import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PositionOfEntitiesFromFile {
    JSONArray entities;
    public PositionOfEntitiesFromFile(String fileName) throws IOException {
        String content = FileLoader.loadResourceFile(fileName);
        JSONObject contentJSON = new JSONObject(content);
        entities = (JSONArray) contentJSON.get("entities");
    }

    public List<Position> getEntityPosition(String type) {
        List<Position> playerPositions = new ArrayList<>();
        for (int i = 0; i < entities.length(); i++) {
            var currEntity = entities.getJSONObject(i);
            if (currEntity.get("type").equals(type)) {
                int x = (int) currEntity.get("x");
                int y = (int) currEntity.get("y");
                Integer layer = null;
                try {
                    // layer throws exception if not found
                    layer = (int) currEntity.get("layer");
                    playerPositions.add(new Position(x, y, layer));
                } catch (JSONException e) {
                    playerPositions.add(new Position(x, y));
                }
            }
        }
        return playerPositions;
    }
}
