package edu.teddys.objects.player;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import edu.teddys.controls.PlayerControl;
import edu.teddys.network.ClientData;
import edu.teddys.states.Game;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Singleton Player. Contains PlayerControl object, the player visuals and
 * is the place for the ClientData storage.
 * 
 * @author skahl
 */
public class Player {

  public static final Integer LOCAL_PLAYER = 0;
  private static Map<Integer, Player> instance = new TreeMap<Integer, Player>();
  Node node;
  Geometry invBoxGeo; // Geometry of the invisible box
  TeddyVisual visual;
  PlayerControl control;
  CapsuleCollisionShape collisionShape;
  ClientData data;

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
    } else {
      /*
       * TODO A network listener should get the appropriate actions from 
       * the client messages and call onAction() and onAnalog().
       */
    }

    game.getBulletAppState().getPhysicsSpace().add(control);

    control.setJumpSpeed(5);
    control.setGravity(5);
    control.setFallSpeed(5);

    // Create the client data object
    data = new ClientData();
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
    if (instance.isEmpty() || instance.get(id) == null) {
      instance.put(id, new Player(id));
    }
    return instance.get(id);
  }

  /**
   * Get a list of Player instances.
   * 
   * @return 
   */
  public static ArrayList<Player> getInstanceList() {
    return new ArrayList<Player>(instance.values());
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
