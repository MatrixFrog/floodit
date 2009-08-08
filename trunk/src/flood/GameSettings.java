package flood;

import java.util.HashMap;
import java.util.Map;

class GameSettings {
  int width, height, numColors;

  GameSettings(int width, int height, int numColors) {
    this.width = width;
    this.height = height;
    this.numColors = numColors;
  }

  static GameSettings get(String name) {
    return gameTypes.get(name);
  }

  static Map<String, GameSettings> gameTypes = new HashMap<String, GameSettings>() {{
    put("Novice", new GameSettings(3, 3, 2));
    put("Beginner", new GameSettings(15, 15, 6));
    put("Intermediate", new GameSettings(30, 30, 6));
    put("Advanced", new GameSettings(40, 40, 6));
    put("Custom", new GameSettings(20, 20, 2));
  }};
}
