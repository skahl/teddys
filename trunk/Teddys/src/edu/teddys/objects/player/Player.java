package edu.teddys.objects.player;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
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
   * @param id The new player ID
   */
  public static void setLocalPlayerId(Integer id) {
    instances.remove(id);
    instances.remove(LOCAL_PLAYER);
    // Initialize the new player object
    Player player = new Player(id);
    // Refresh the ID
    LOCAL_PLAYER = id;
    instances.put(id, player);
  }
  
  /**
   * 
   * Remove the player specified by the ID. Note that the node will be removed from the
   * Game world as well.
   * 
   * @param id The player ID
   */
  public static void removePlayer(Integer id) {
    Player oldPlayer = Player.getInstance(id);
    Game.getInstance().removeSpatial(Game.getInstance().getRootNode(), oldPlayer.getNode());
    Game.getInstance().getBulletAppState().getPhysicsSpace().remove(oldPlayer.getPlayerControl());
    instances.remove(id);
  }
  
  public static void setInstanceList(Map<Integer,Player> playerMap) {
    //TODO !
  }
  
  /**
   * DON'T CALL THIS!!! THIS IS FOR SERIALIZING PURPOSES!
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

    control = new PlayerControl(node, collisionShape, 0.02f, visual);
    if (id == LOCAL_PLAYER) {
      //TODO check
//      control.registerWithInput(game.getInputManager());
    }

    game.getBulletAppState().getPhysicsSpace().add(control);

    control.setJumpSpeed(5);
    control.setGravity(5);
    control.setFallSpeed(5);

    // Set the (network) client ID
    data.setId(id);
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
