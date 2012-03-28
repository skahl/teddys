package edu.teddys.map;

import com.jme3.asset.AssetLoadException;
import edu.teddys.MegaLogger;
import edu.teddys.states.Game;

/**
 * The GameLoader takes the package of a map and presents the game config to
 * the server and the model of the map to the client.
 * 
 * @author skahl
 */
public class GameLoader {

  private GameMap gameMap;
  private GameMapConfig gameMapCfg;

  public GameLoader(String name, String packagePath, Game game) {

    try {

      //game.getApp().getAssetManager().registerLocator(packagePath, ZipLocator.class.getName());

//      gameMap = new GameMap(name + ".j3o", game);
//      gameMapCfg = new GameMapConfig(name + ".cfg");
      gameMap = new GameMap(packagePath, game);
      gameMapCfg = new GameMapConfig(packagePath);

    } catch (AssetLoadException e) {
      MegaLogger.getLogger().fatal(new Throwable("Map could not be loaded!", e));
    }
  }

  public GameMap getGameMap() {
    return gameMap;
  }

  public GameMapConfig getGameMapConfig() {
    return gameMapCfg;
  }
}
