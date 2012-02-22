package edu.teddys.objects.player;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import edu.teddys.controls.PlayerControl;
import edu.teddys.network.ClientData;
import edu.teddys.states.Game;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Singleton Player
 * @author skahl
 */
public class Player {

  public static final Integer LOCAL_PLAYER = 0;
  private static Map<Integer, Player> instance = new TreeMap<Integer, Player>();
  Node node;
  TeddyVisual visual;
  PlayerControl control;
  CapsuleCollisionShape collisionShape;
  BoundingBox boundingBox;
  ClientData data;

  public ClientData getData() {
    return data;
  }

  public void setData(ClientData data) {
    this.data = data;
  }

  private Player(Integer id) {

    Game game = Game.getInstance();

    // Use the toString() method to generate a quite uniquely identified Node
    node = new Node("player" + id.toString());
    visual = new TeddyVisual(node, game.getAssetManager());

    // model bounds (make it shootable)
    boundingBox = new BoundingBox(visual.getGeo().getLocalTranslation(), visual.getWidth(), visual.getHeight(), 1.0f);
    visual.getBox().setBound(boundingBox);
    visual.getBox().updateBound();

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

  public PlayerControl getPlayerControl() {
    return control;
  }

  public Node getNode() {
    return node;
  }

  public static Player getInstance(Integer id) {
    if (instance.isEmpty() || instance.get(id) == null) {
      instance.put(id, new Player(id));
    }
    return instance.get(id);
  }

  public static ArrayList<Player> getInstanceList() {
    return new ArrayList<Player>(instance.values());
  }

  public static Node getPlayerTree() {
    ArrayList<Player> pl = getInstanceList();
    Node node = new Node("playerTree");
    for (Player p : pl) {
      Spatial tmp = p.getNode().clone();
      tmp.getWorldTranslation().setZ(0f);
      node.attachChild(tmp);
    }
    return node;
  }

  /**
   * 
   * Filters a node containing the specified playerNode name.
   * 
   */
  public static Node getPlayerTree(String playerNodeName) {
    ArrayList<Player> pl = getInstanceList();
    Node node = new Node("playerTree");
    for (Player p : pl) {
      if (p.getNode().getName().equals(playerNodeName)) {
        continue;
      }
      Spatial tmp = p.getNode().clone();
//      tmp.getWorldTranslation().setZ(0f);
      node.attachChild(tmp);
    }
    return node;
  }

  public static Player getPlayerByNode(String name) {
    System.out.println("ARG:" + name);
    System.out.println(getInstanceList().toString());
    for (Player p : getInstanceList()) {
      System.out.println("p:" + p.getNode().getName());
      if (p.getNode().getName().equals(name)) {
        return p;
      }
    }
    return null;
  }
}
