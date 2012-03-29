package edu.teddys.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.BasicShadowRenderer;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.BaseGame;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.callables.AddCharacterControlToPhysicsSpaceCallable;
import edu.teddys.callables.AddNodeToPhysicsSpaceCallable;
import edu.teddys.callables.AddPhysicsCollisionListener;
import edu.teddys.callables.RemoveCharacterControlFromPhysicsSpace;
import edu.teddys.callables.RemoveNodeFromPhysicsSpace;
import edu.teddys.callables.RemovePhysicsCollisionListener;
import edu.teddys.callables.SetPositionOfTeddyCallable;
import edu.teddys.controls.GeometryCollisionListener;
import edu.teddys.map.GameLoader;
import edu.teddys.map.GameMapConfig.PositionBoundary;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ManMessageSetPosition;
import edu.teddys.objects.player.Player;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import org.apache.commons.math.random.RandomDataImpl;

/**
 *
 * @author skahl
 */
public class Game extends AbstractAppState {

  private static Game instance = null;
  private BaseGame app;
  private AppStateManager stateManager;
  private InputManager inputManager;
  private BulletAppState bulletAppState;
  private Node rootNode;
  private BasicShadowRenderer shadowRenderer; // Shadow rendering
  private GameLoader gameLoader;
  private boolean paused;
  private Entry<String, String> levelData = new SimpleEntry<String, String>("firstlevel", "Models/firstlevel/");
  // TESTING PHYSICS PROBLEMS
  private GeometryCollisionListener geoColListener;

  //private boolean enabled;
  protected Game() {
    super();
  }

  public static Game getInstance() {
    if (instance == null) {
      instance = new Game();
    }
    return instance;
  }
  // ActionListener
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {
    }
  };

//  @Override
//  public boolean isEnabled() {
//    return enabled;
//  }
  @Override
  public void setEnabled(boolean isActive) {
    super.setEnabled(isActive);
    if (isActive) {
      // activate

      // here one could attach and detach the whole scene graph
      //this.app.getRootNode().attachChild(rotationNode);

      // attach keys
      initKeys(true);

      if (Player.getInstance(Player.LOCAL_PLAYER).getHUD() != null) {
        Player.getInstance(Player.LOCAL_PLAYER).getHUD().show();
      }
      if (Player.getInstance(Player.LOCAL_PLAYER).getCursor() != null) {
        addSpatial(this.app.getGuiNode(), Player.getInstance(Player.LOCAL_PLAYER).getCursor());
      }

    } else {

      // detach keys
      initKeys(false);

      if (Player.getInstance(Player.LOCAL_PLAYER).getHUD() != null) {
        Player.getInstance(Player.LOCAL_PLAYER).getHUD().hide();
      }
      if (Player.getInstance(Player.LOCAL_PLAYER).getCursor() != null) {
        removeSpatial(this.app.getGuiNode(), Player.getInstance(Player.LOCAL_PLAYER).getCursor());
      }
    }
  }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    if (!isInitialized()) {
      super.initialize(stateManager, app);
      this.app = (BaseGame) app;
      this.stateManager = stateManager;
      this.inputManager = this.app.getInputManager();
      this.rootNode = this.app.getRootNode();


      this.paused = false;
      super.setEnabled(false);


      // init physics  renderstate
      bulletAppState = new BulletAppState();
      stateManager.attach(bulletAppState);

      // shed some light
      Vector3f sunDirection = new Vector3f(-1f, -1f, -1f);
      sunDirection.normalizeLocal();

//            DirectionalLight sunL = new DirectionalLight();
//            sunL.setColor(ColorRGBA.White.mult(0.7f));
//            sunL.setDirection(sunDirection);
//            rootNode.addLight(sunL);
//
//            AmbientLight sunA = new AmbientLight();
//            sunA.setColor(ColorRGBA.White.mult(0.5f));
//            rootNode.addLight(sunA);

      // init shadow renderstate
      shadowRenderer = new BasicShadowRenderer(this.app.getAssetManager(), 256);
      shadowRenderer.setDirection(sunDirection);
      this.app.getViewPort().addProcessor(shadowRenderer);

      // physics debug (shows collission meshes):

      if (GameSettings.DEBUG) {
        // debug switch not working
        bulletAppState.getPhysicsSpace().enableDebug(this.app.getAssetManager());
      }

      MegaLogger.getLogger().debug("New game instance created.");
    }
  }

  /**
   * 
   * Add a spatial (such as Node) to the current world.
   * 
   * @param parent  The node to which the child should be added.
   * @param child  The spatial to be added.
   */
  public void addSpatial(Node parent, Spatial child) {
    getApp().enqueue(new AttachToNodeCallable(parent, child));
  }

  /**
   * 
   * Remove a spatial from the parent.
   * 
   * @param parent  The node from which the spatial should be removed.
   * @param child The spatial to be removed.
   */
  public void removeSpatial(Node parent, Spatial child) {
    getApp().enqueue(new DetachFromNodeCallable(parent, child));
  }

  void addCharacterControlToPhysicsSpace(CharacterControl control) {
    getApp().enqueue(new AddCharacterControlToPhysicsSpaceCallable(getBulletAppState().getPhysicsSpace(), control));
  }

  void removeCharacterControlFromPhysicsSpace(CharacterControl control) {
    getApp().enqueue((new RemoveCharacterControlFromPhysicsSpace(getBulletAppState().getPhysicsSpace(), control)));
  }

  public void addCollisionListenerToPhysicsSpace(PhysicsCollisionListener listener) {
    getApp().enqueue(new AddPhysicsCollisionListener(getBulletAppState().getPhysicsSpace(), listener));
  }

  public void removeCollisionListenerFromPhysicsSpace(PhysicsCollisionListener listener) {
    getApp().enqueue(new RemovePhysicsCollisionListener(getBulletAppState().getPhysicsSpace(), listener));
  }

  public void addNodeToPhysicsSpace(Node node) {
    getApp().enqueue(new AddNodeToPhysicsSpaceCallable(getBulletAppState().getPhysicsSpace(), node));
  }

  public void removeNodeFromPhysicsSpace(Node node) {
    getApp().enqueue((new RemoveNodeFromPhysicsSpace(getBulletAppState().getPhysicsSpace(), node)));
  }

  /**
   * 
   * Create a random position and enqueue the change of the position of the specified Player.
   * 
   * @param player The player whose position is set.
   */
  public void setRandomPlayerPosition(Player player) {
    RandomDataImpl rnd = new RandomDataImpl();
    
    int numSpawnSpaces = gameLoader.getGameMapConfig().getPositionBoundaries().size();
    
    // randomly chose one of the spawn spaces
    int chosenSpawnSpace = (int) rnd.nextLong(0,numSpawnSpaces-1);
    // from that chosen spawn space, randomly chose the spawn coordinates
    PositionBoundary pb = gameLoader.getGameMapConfig().getPositionBoundaries().get(chosenSpawnSpace);
    
    Vector3f pos = new Vector3f((float)rnd.nextLong((long)pb.getLowerBound().x, (long)pb.getUpperBound().x), 
            (float)rnd.nextLong((long)pb.getLowerBound().y, (long)pb.getUpperBound().y),
            pb.getUpperBound().z);
    
    // this is the first position of the Teddy, so initialize a new List
    SetPositionOfTeddyCallable setPos = new SetPositionOfTeddyCallable(player, pos, true);
    getApp().enqueue(setPos);
    MegaLogger.getLogger().debug("Position of the player has been randomly set to "
            + pos + " (ID: " + player.getData().getId() + ")");
    ManMessageSetPosition posMsg = new ManMessageSetPosition();
    posMsg.getPositions().put(player.getData().getId(), pos);
    posMsg.setFixed(true);
    // In case this is the server instance, send the position data
    TeddyServer.getInstance().send(posMsg);
  }

  /**
   * 
   * Add a player to the root node of the game world. If it already exists,
   * just return.
   * 
   * @param player  The Player to be added to the world.
   */
  public void addPlayerToWorld(Player player) {
    if (getRootNode().hasChild(player.getNode())) {
      return;
    }
    MegaLogger.getLogger().debug("New player should be added to the world. Creating the requests.");
    // add to the world
    addSpatial(getRootNode(), player.getNode());
    addCharacterControlToPhysicsSpace(player.getPlayerControl());

    MegaLogger.getLogger().debug("IMPORTANT: Adding Geometry Collision Listener on player.getNode()!");
    geoColListener = new GeometryCollisionListener(player.getNode(), null);
    geoColListener.setListen(true);
  }

  public void removePlayerFromWorld(Player player) {
    if (getRootNode().hasChild(player.getNode())) {
      MegaLogger.getLogger().debug("Player " + player + " should be removed from the world.");
      removeCharacterControlFromPhysicsSpace(player.getPlayerControl());
      removeSpatial(getRootNode(), player.getNode());
    }
  }

  public void addMapModel(Node mapModel) {
    MegaLogger.getLogger().debug("Map model " + mapModel + " should be added to the Root Node and to the PhysicsSpace.");
    addSpatial(getRootNode(), mapModel);
    addNodeToPhysicsSpace(mapModel);

  }

  @Override
  public void cleanup() {
    super.setEnabled(false);
    super.cleanup();
  }

  public void initKeys(boolean attach) {

    if (attach) {
      // add key mappings
      //inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
      //inputManager.addMapping(MappingEnum.PARTICLE_TRIGGER.name(), new KeyTrigger(KeyInput.KEY_SPACE));
      // add the action listener
      //inputManager.addListener(actionListener, new String[]{MappingEnum.PARTICLE_TRIGGER.name()});
      // add the analog listener
      //inputManager.addListener(analogListener, new String[]{"ParticleTrigger"});
    } else {
      //inputManager.deleteMapping("Pause");
      //inputManager.deleteMapping(MappingEnum.PARTICLE_TRIGGER.name());
    }

  }

  public BaseGame getApp() {
    return app;
  }

  public Node getRootNode() {
    return rootNode;
  }

  public BulletAppState getBulletAppState() {
    return bulletAppState;
  }

  public AssetManager getAssetManager() {
    return app.getAssetManager();
  }

  public InputManager getInputManager() {
    return inputManager;
  }

  public void loadGameMap(String levelName, String mapPath) {
    MegaLogger.getLogger().debug("Loading map (" + levelName + "): " + mapPath);
    levelData = new SimpleEntry<String, String>(levelName, mapPath);
    gameLoader = new GameLoader(levelName, mapPath);
    addMapModel(gameLoader.getGameMap().getMapModel());
  }

  public Entry<String, String> getLevelData() {
    return levelData;
  }
}
