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
import edu.teddys.controls.PlayerControl;
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
    if(id == LOCAL_PLAYER) {
      // no change
      return;
    }
    //TODO check if removing all players is better ...
    if(instances.containsKey(id)) {
      // Remove the old player
      Game.getInstance().removePlayerFromWorld(Player.getInstance(id));
      instances.remove(id);
    }
    // Get the current instance
    Player localPlayer = Player.getInstance(LOCAL_PLAYER);
    // Instead of copying all data, rename the node 
    // and change the position in the instances List
    localPlayer.getNode().setName("player" + id.toString());
    localPlayer.getData().setId(id);
    // Check if the camera is null. If so, create a new one ...
    CameraNode camNode = Game.getInstance().getCamNode();
    if(camNode == null) {
      Game.getInstance().initCamNode();
      MegaLogger.getLogger().debug("CameraNode created");
      // reload
      camNode = Game.getInstance().getCamNode();
    }
    // ... and attach it to the user's Teddy
    if(!localPlayer.getNode().hasChild(camNode)) {
      MegaLogger.getLogger().debug("CameraNode has not been attached yet.");
      Vector3f dir = localPlayer.getNode().getWorldTranslation().add(0, 0.75f, 0);
      camNode.lookAt(dir, new Vector3f(0, 1, 0));
      Game.getInstance().addSpatial(localPlayer.getNode(), camNode);
    }
    instances.put(id, localPlayer);
    // Refresh the list since the ID has changed
    instances.remove(LOCAL_PLAYER);
    // Update the LOCAL_PLAYER ID
    LOCAL_PLAYER = id;
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

    control = new PlayerControl(collisionShape, 0.02f, visual);
    if (id == LOCAL_PLAYER) {
      //TODO check
//      control.registerWithInput(game.getInputManager());
    }

    control.setJumpSpeed(5);
    control.setGravity(5);
    control.setFallSpeed(5);
    // The location of the CharacterControl Spatial should be the same as from the Player's node
    control.setPhysicsLocation(node.getWorldTranslation());
    // Set a binding
    node.addControl(control);

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
}
