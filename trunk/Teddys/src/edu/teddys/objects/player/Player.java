package edu.teddys.objects.player;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import edu.teddys.MegaLogger;
import edu.teddys.callables.TeddyDeadCallable;
import edu.teddys.controls.PlayerControl;
import edu.teddys.input.CrosshairControl;
import edu.teddys.network.ClientData;
import edu.teddys.states.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Singleton Player. Contains PlayerControl object, the player visuals and
 * is the place for the ClientData storage.
 * 
 * @author skahl
 */
@Serializable
public class Player {

  /**
   * The local player ID will be set to the network ID
   * if the connection to a game server has been established.
   */
  public static transient Integer LOCAL_PLAYER = 0;
  /**
   * The map of instances. It holds all players' data.
   */
  private static Map<Integer, Player> instances = new TreeMap<Integer, Player>();
  transient Node node;
  transient Geometry invBoxGeo; // Geometry of the invisible box
  transient TeddyVisual visual;
  transient PlayerControl control;
  transient CapsuleCollisionShape collisionShape;
  private CameraNode camNode = null;
  private CrosshairControl cameraControl;
  ClientData data = new ClientData();

  /**
   * ClientData getter method.
   * 
   * @return The client data.
   */
  public ClientData getData() {
    return data;
  }

  /**
   * ClientData setter method.
   * 
   * @param data 
   */
  public void setData(ClientData data) {
    this.data = data;
  }

  /**
   * 
   * Note: If a player with the specified ID already exists, it will be removed!
   * Also, the old instance of the local player will be removed!
   * 
   * This method is only called (until now ...) by clientConnected() in TeddyClient!
   * 
   * @param id The new player ID
   */
  public synchronized static void setLocalPlayerId(Integer id) {
    if (id == LOCAL_PLAYER) {
      // no change
      return;
    }
    //TODO check if removing all players is better ...
    if (instances.containsKey(id)) {
      Player oldPlayer = Player.getInstance(id);
      // Remove the old player
      Game.getInstance().removePlayerFromWorld(oldPlayer);
      // Remove the camera Node
      if(oldPlayer.camNode != null) {
        Game.getInstance().removeSpatial(oldPlayer.getNode(), oldPlayer.camNode);
        oldPlayer.camNode = null;
      }
      instances.remove(id);
    }
    // Get the current instance
    Player localPlayer = Player.getInstance(LOCAL_PLAYER);
    // Instead of copying all data, rename the node 
    // and change the position in the instances List
    localPlayer.getNode().setName("player" + id.toString());
    localPlayer.getData().setId(id);
    localPlayer.initCamNode();
    instances.put(id, localPlayer);
    // Refresh the list since the ID has changed
    instances.remove(LOCAL_PLAYER);
    // Update the LOCAL_PLAYER ID
    LOCAL_PLAYER = id;
  }

  /**
   * 
   * Initialize or update the CameraNode and CrosshairControl.
   * 
   * If the player in the CrosshairControl is different from the previous one,
   * update the reference.
   * 
   * @see CameraNode
   * @see CrosshairControl
   * 
   * @param player The player instance to which the camera should be attached.
   */
  protected void initCamNode() {
    
    MegaLogger.getLogger().debug("Initializing CameraNode ...");
    
    Game gameInstance = Game.getInstance();
    
    if(camNode == null) {
      // Camera
      camNode = new CameraNode("Camera", gameInstance.getApp().getCamera());
      camNode.setControlDir(ControlDirection.SpatialToCamera);
      // initial distance between camera and player
      camNode.move(0, 1, 8);
      gameInstance.addSpatial(getNode(), camNode);
      MegaLogger.getLogger().debug("New CameraNode created.");
    }

    // Update the camera setting
    Vector3f dir = getNode().getWorldTranslation().add(0, 0.75f, 0);
    camNode.lookAt(dir, new Vector3f(0, 1, 0));

    cameraControl = new CrosshairControl(camNode, this,
            gameInstance.getCursor(),
            gameInstance.getApp().getSettings().getWidth(),
            gameInstance.getApp().getSettings().getHeight());
    cameraControl.registerWithInput(gameInstance.getInputManager());
    MegaLogger.getLogger().debug("New CameraControl created.");
  }

  /**
   * 
   * Remove the player specified by the ID. Note that the node will be removed from the
   * Game world as well.
   * 
   * @param id The player ID
   */
  public synchronized static void removePlayer(Integer id) {
    Player oldPlayer = Player.getInstance(id);
    Game.getInstance().removePlayerFromWorld(oldPlayer);
    instances.remove(id);
  }

  public synchronized static void setInstanceList(Map<Integer, Player> playerMap) {
    //TODO Problems: The Nodes are not transmitted
    //TODO What about lag compensation? use smoothed movements etc ...
//    instances = playerMap;
    //TODO refresh the positions in the local world if a game is active
  }

  /**
   * DON'T CALL THIS!!! THIS IS FOR SERIALIZING PURPOSES ONLY!
   */
  public Player() {
  }

  /**
   * Player constructor. Initializes controls, visuals and physics of the player.
   * 
   * @param id Players are differentiated by this ID.
   */
  private Player(Integer id) {

    Game game = Game.getInstance();

    // Use the toString() method to generate a quite uniquely identified Node
    node = new Node("player" + id.toString());
    visual = new TeddyVisual(node, game.getAssetManager());

    // attach a cube to the model, so it becomes shootable
    Box invisibleBox = new Box(0.3f, 0.3f, 0.5f);
    invBoxGeo = new Geometry("player" + id.toString(), invisibleBox);
    Material red = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    red.setColor("Color", ColorRGBA.Red);
    invBoxGeo.setMaterial(red);
    invBoxGeo.setCullHint(Spatial.CullHint.Always); //make invisible
    node.attachChild(invBoxGeo);


    // physics
    collisionShape = new CapsuleCollisionShape(visual.getWidth() * 0.3f, visual.getHeight() * 0.35f, 1);

    control = new PlayerControl(this, collisionShape, 0.02f, visual);
    if (id == LOCAL_PLAYER) {
      // This is handled by the ClientTimerThread
//      control.registerWithInput(game.getInputManager());
    }

    control.setJumpSpeed(5);
    control.setGravity(5);
    control.setFallSpeed(5);

    // Set a binding
    node.addControl(control);


    // The location of the CharacterControl Spatial should be the same as from the Player's node
    control.setPhysicsLocation(node.getWorldTranslation().add(new Vector3f(0f, 0f, -1.2f)));


    // Set the (network) client ID
    data.setId(id);

    MegaLogger.getLogger().debug("New player created (id=" + id + ")");
  }

  /**
   * playerControl getter.
   * 
   * @return 
   */
  public PlayerControl getPlayerControl() {
    return control;
  }

  /**
   * playerVisual getter.
   * @return 
   */
  public TeddyVisual getPlayerVisual() {
    return visual;
  }

  /**
   * Since each player has a Node of its own. This is where you can get it.
   * 
   * @return 
   */
  public Node getNode() {
    return node;
  }

  /**
   * Player is a singleton. Use this to obtain the object by player ID.
   * 
   * @param id
   * @return 
   */
  public static Player getInstance(Integer id) {
    if (instances.isEmpty() || instances.get(id) == null) {
      instances.put(id, new Player(id));
    }
    return instances.get(id);
  }

  /**
   * Get a list of Player instances.
   * 
   * @return 
   */
  public static List<Player> getInstanceList() {
    return new ArrayList<Player>(instances.values());
  }

  public static Map<Integer, Player> getInstances() {
    return instances;
  }

  /**
   * This method creates a Node, to which clones of Player nodes are attached.
   * 
   * @return 
   */
  public static Node getPlayerTree() {
    Node node = new Node("playerTree");
    for (Player p : getInstanceList()) {
      Spatial tmp = p.getNode().clone();
      node.attachChild(tmp);
    }
    return node;
  }

  /**
   * Returns a Node tree of all player nodes, except the given player node.
   * 
   * @return
   */
  public static Node getPlayerTree(String playerNodeName) {
    Node node = new Node("playerTree");
    for (Player p : getInstanceList()) {
      if (p.getNode().getName().equals(playerNodeName)) {
        continue;
      }
      Spatial tmp = p.getNode().clone();
      node.attachChild(tmp);
    }
    return node;
  }

  /**
   * Return the Player instance given the wanted Player's name.
   * 
   * @param name
   * @return 
   */
  public static Player getPlayerByNode(String name) {
    for (Player p : getInstanceList()) {
      if (p.getNode().getName().equals(name)) {
        return p;
      }
    }
    return null;
  }

  /**
   * 
   * Adds the specified damage value to the current teddy. Checks also if
   * the teddy is dead from now on and notifies all listeners of that fact 
   * if so.
   * 
   * @param damage Positive value as it is the damage to the teddy
   */
  public void addDamage(Integer damage) {
    Integer newHealth = getData().getHealth() - damage;
    if (newHealth > 0) {
      getData().setHealth(newHealth);
      return;
    }
    getData().setHealth(0);
    
    Game.getInstance().getApp().enqueue(new TeddyDeadCallable(Player.getInstance(getData().getId())));
  }
}
