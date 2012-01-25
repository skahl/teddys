package edu.teddys.objects.player;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.scene.Node;
import edu.teddys.MegaLogger;
import edu.teddys.input.PlayerControl;
import edu.teddys.network.ClientData;
import edu.teddys.states.Game;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 *  Singleton Player
 * @author skahl
 */
public class Player {

  public static final Integer LOCAL_PLAYER = 0;
  private static Player instance = null;
  Node node;
  TeddyVisual visual;
  PlayerControl control;
  CapsuleCollisionShape collisionShape;
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
    node = new Node("player"+id);
    visual = new TeddyVisual(node, game.getAssetManager());

    // physics
    collisionShape = new CapsuleCollisionShape(visual.getWidth() * 0.3f, visual.getHeight() * 0.35f, 1);

    control = new PlayerControl(node, collisionShape, 0.02f);
    if(id == LOCAL_PLAYER) {
      //TODO check
      control.registerWithInput(game.getInputManager());
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
  }

  public PlayerControl getPlayerControl() {
    return control;
  }

  public Node getNode() {
    return node;
  }

  public static Player getInstance(Integer id) {
    if (instance == null) {
      instance = new Player(id);
    }
    return instance;
  }

  /**
   * 
   * New input data has arrived, that means some events can be triggered,
   * such as jumps, jetpack activation etc.
   * 
   * @param input A queue of actions gathered in the last time frame.
   */
  public void newInput(LinkedList<Entry<String, Object>> input) {
    MegaLogger.getLogger().debug("New data received!!");
    for(Entry<String, Object> entry : input) {
      if(entry.getValue() instanceof Boolean) {
        // this is an action
        //TODO check! tpf must be transmitted?
        getPlayerControl().onAction(entry.getKey(), (Boolean)entry.getValue(), 1f);
      } else if(entry.getValue() instanceof Float) {
        //TODO check!
        getPlayerControl().onAnalog(entry.getKey(), (Float)entry.getValue(), 1f);
      }
    }
  }
}
