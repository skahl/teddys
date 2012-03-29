package edu.teddys.map;

import com.jme3.asset.AssetLoadException;
import com.jme3.light.Light;
import edu.teddys.MegaLogger;
import edu.teddys.callables.AttachLightCallable;
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

  public GameLoader(String name, String packagePath) {

    try {

      //game.getApp().getAssetManager().registerLocator(packagePath, ZipLocator.class.getName());

//      gameMap = new GameMap(name + ".j3o", game);
//      gameMapCfg = new GameMapConfig(name + ".cfg");
      gameMap = new GameMap(packagePath+name);
      gameMapCfg = new GameMapConfig(packagePath+name);
      
      if(gameMapCfg.parsingSuccessfull()) {
        // set backgroundcolor from config
        Game.getInstance().getApp().getViewPort().setBackgroundColor(gameMapCfg.getBackgroundColor());
        // set lights from config
        for(Light l : gameMapCfg.getLights()) {
          Game.getInstance().getApp().enqueue(new AttachLightCallable(l));
        }
      } else {
        MegaLogger.getLogger().fatal(new Throwable("Map config could not be parsed successfully!"));
      }

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
