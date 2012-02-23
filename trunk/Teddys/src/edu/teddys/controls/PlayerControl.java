package edu.teddys.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.hud.HUDController;
import edu.teddys.input.AnalogControllerEnum;
import edu.teddys.input.ControllerEvents;
import edu.teddys.input.InputType;
import edu.teddys.input.InputTuple;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.objects.player.Player;
import edu.teddys.objects.player.TeddyVisual;
import edu.teddys.objects.weapons.DeafNut;
import edu.teddys.objects.weapons.HolyWater;
import edu.teddys.objects.weapons.Rocket;
import edu.teddys.objects.weapons.Weapon;
import edu.teddys.states.Game;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/** Control class responsible for character controls and visuals, based on input.
 *
 * @author besient,skahl,cm
 */
public class PlayerControl extends CharacterControl implements AnalogListener, ActionListener {

  private TeddyVisual visual;
  // screen positions of cursor and player
  private Vector2f vectorPlayerToCursor = new Vector2f(1f, 0f);
  private float moveSpeed = 2f;
  private boolean jetpackActive;
  private float jetpackDischargeRate = 75f;
  private float jetpackChargeRate = 25f;
  private float totalJetpackEnergy = 100f;
  private float currentEnergy = totalJetpackEnergy;
  private float oldGravity = 4f;
  private InputManager input;
  private Vector3f left, right;
  // player control input from server
  private LinkedList<InputTuple> serverControlInput;
  private short controlTimer = 0;
  private short jetpackTimer = 0;
  private short weaponTimer = 0;
  private short hudUpdateTimer = 0;
  
  
  /**
   * PlayerControl constructor. Sets physics properties.
   * 
   * @param player
   * @param collisionShape
   * @param stepHeight
   * @param vis 
   */
  public PlayerControl(Spatial player, CollisionShape collisionShape, float stepHeight, TeddyVisual vis) {
    super(collisionShape, stepHeight);
    setPhysicsLocation(player.getWorldTranslation());
    player.addControl(this);

    visual = vis;
    serverControlInput = new LinkedList<InputTuple>();

    left = new Vector3f(-1, 0, 0);
    right = new Vector3f(1, 0, 0);

  }

  /** 
   * Register KeySet Listeners
   * 
   * @param input InputManager
   */
  public void registerWithInput(InputManager input) {
    this.input = input;

    Map<String, List<Trigger>> map = ControllerEvents.getAllEvents();

    input.addListener(this, map.keySet().toArray(new String[map.keySet().size()]));
  }

  /** 
   * Analog Control listeners. Walking, Jetpack and Shooting.
   * 
   * @param name
   * @param value
   * @param tpf 
   */
  public void onAnalog(String name, float value, float tpf) {
    if (name.equals(AnalogControllerEnum.MOVE_LEFT.name())) {

      // reset control timer
      controlTimer = 0;

      // better for physics than warp(getPhysicsLocation().add(vel));
      setWalkDirection(left.mult(moveSpeed * tpf));

      // visual
      if (!jetpackActive && onGround()) {
        visual.runLeft();
      } else {
        visual.stand();
      }

    } else if (name.equals(AnalogControllerEnum.MOVE_RIGHT.name())) {

      // reset control timer
      controlTimer = 0;

      // better for physics than warp(getPhysicsLocation().add(vel));
      setWalkDirection(right.mult(moveSpeed * tpf));

      // visual
      if (!jetpackActive && onGround()) {
        visual.runRight();
      } else {
        visual.stand();
      }

    }

    if (name.equals(AnalogControllerEnum.WEAPON.name())) {

      // reset weapon timer
      weaponTimer = 0;

      // visual
      visual.getWeapon().shoot();
      
      Ray ray = new Ray(getPhysicsLocation(), visual.getWeapon().getVector());
      // I could do that!
      //ray.setLimit(); ??? 
      CollisionResults hits = new CollisionResults();     
      
      Game.getInstance().getRootNode().collideWith(ray, hits);
      
      // Check if a Box was hit, since Box are only used by Players...
      if(hits.size() > 1) {// && hits.getCollision(2).getGeometry().getClass().equals(Box.class)) {
        Geometry p = hits.getCollision(1).getGeometry();
        String nodeName = p.getParent().getName();
        
        // I found no other way than to compare a string... :(
        if(p.getParent().getName().contains("player")) {
        
          
          //TODO Now calculate the specific damage dependend from the current weapon
          /*TODO If there is no other reason than for checking the weapon's range,
           * one could use ray.setLimit() to limit the intersection check distance
           * 
           */
          float distance = hits.getCollision(1).getDistance();
          MegaLogger.getLogger().debug("Distance: " + distance);
          //Vector3f iPoint = hits.getCollision(1).getContactPoint();
          
          // Assume we have a honey brew yet
          Weapon w = new DeafNut();
          RandomDataImpl rnd = new RandomDataImpl();
          Double weaponDamage = Math.abs(rnd.nextGaussian(w.getBaseDamage(), GameSettings.DAMAGE_SIGMA));
          Double weaponAccuracy = Math.abs(rnd.nextGaussian(w.getAccuracy(), GameSettings.DAMAGE_SIGMA));
          Double damage = GameSettings.DAMAGE_MAX * weaponDamage * weaponAccuracy;
          MegaLogger.getLogger().info(String.format("m=%f,b=%f,a=%f",
                  GameSettings.DAMAGE_MAX,
                  weaponDamage,
                  weaponAccuracy));
          int resDamage = (int) Math.ceil(damage);
          String hitmsg = " Damage: " + resDamage;
          ManMessageSendDamage dmgMsg = new ManMessageSendDamage(
                  Player.getPlayerByNode(nodeName).getData().getId(), resDamage);
          //TODO this should only be done by the server instance!
          
          MegaLogger.getLogger().info("Hit: "+nodeName+hitmsg);
          TeddyServer.getInstance().send(dmgMsg);
        }
      }
    }

    if (name.contains(AnalogControllerEnum.JETPACK.name())) {

      jetpackTimer = 0;

      if (!jetpackActive) {
        startJetpack();
      }
    }

  }

  /** 
   * Action Control listener.
   * 
   * @param name
   * @param isPressed
   * @param tpf 
   */
  public void onAction(String name, boolean isPressed, float tpf) {
//    if (name.equals(ActionControllerEnum.JETPACK.name())) {
//      if (!jetpackActive && isPressed) {
//        startJetpack();
//      } else {
//        stopJetpack();
//      }
//    }
  }

  /** 
   * Start Jetpack firing, with gravity controls and visuals.
   * 
   */
  private void startJetpack() {
    if (currentEnergy > 25f) {
      oldGravity = getGravity();
      setGravity(-getGravity());
      jetpackActive = true;

      visual.stand();
      visual.getJetpack().setEnabled(true);
    } else {
      stopJetpack();
    }
  }

  /** 
   * Set everything back to normal after Jetpack use.
   * 
   */
  private void stopJetpack() {
    setGravity(oldGravity);
    jetpackActive = false;

    visual.getJetpack().setEnabled(false);
  }

  /** 
   * Gravity setter method, for player gravity.
   * 
   * @param value 
   */
  @Override
  public void setGravity(float value) {
    super.setGravity(value);
  }

  /** 
   * Usually only used on Crosshair movement updates!
   * 
   * Feed udpated Player and Cursor positions to this control class.
   * 
   * @param playerPos
   * @param cursorPos 
   */
  public void setScreenPositions(Vector2f playerPos, Vector2f cursorPos) {
    vectorPlayerToCursor = (cursorPos.subtract(playerPos)).normalizeLocal();
    visual.getWeapon().setVector(new Vector3f(vectorPlayerToCursor.x, vectorPlayerToCursor.y, 0f));
  }

  /**
   * This control's update method. Is being called from JME, automatically.
   * Basically forwards and processes the input queue.
   * 
   * @param tpf 
   */
  @Override
  public void update(float tpf) {
    super.update(tpf);

    if (jetpackActive) {
      if (currentEnergy > 0) {
        currentEnergy -= jetpackDischargeRate * tpf;
      } else {
        stopJetpack();
      }
    } else {
      if (currentEnergy < totalJetpackEnergy) {
        currentEnergy += jetpackChargeRate * tpf;
      }
    }



    // increase control timer and act on it, if necessary
    controlTimer++;
    if (controlTimer > 5) {
      setWalkDirection(Vector3f.ZERO);
      visual.stand(); // enough running without input, stand still!

      controlTimer = 0;
    }

    // increase jetpack timer and act on it
    jetpackTimer++;
    if (jetpackTimer > 5) {
      stopJetpack();

      jetpackTimer = 0;
    }

    // increase weapon timer
    weaponTimer++;
    if (weaponTimer > 10) {
      visual.getWeapon().stop();
      weaponTimer = 0;
    }

    // increase HUD timer and act on it, if necessary
    hudUpdateTimer++;
    if(hudUpdateTimer > 4) {
        hudUpdateTimer = 0;
        
        // calculate the angle from the default up vector of the player, to the cursor's position:
        //angleToDefaultVector = defaultVectorPlayerToCursor.angleBetween(vectorPlayerToCursor);
        //MegaLogger.getLogger().info(new Throwable(angleToDefaultVector.toString()));
        
        // inform the visual representation of this player about the view vector
        visual.setViewVector(vectorPlayerToCursor);
        
        // update HUD 
        HUDController.getInstance().setJetpackEnergy((int) currentEnergy);
    }

    InputTuple entry = null;
    while (serverControlInput.size() > 0) {

      entry = serverControlInput.pop();
//    MegaLogger.getLogger().debug("input: " + entry);
      if (entry.getType() == InputType.Analog) {
        onAnalog(entry.getKey(), (Float) entry.getValue(), entry.getTpf());
      } else if (entry.getType() == InputType.Action) {
        if (entry.getValue() instanceof Boolean) {
          onAction(entry.getKey(), (Boolean) entry.getValue(), entry.getTpf());
        } else {
          MegaLogger.getLogger().error(new Throwable("Action event invalid! Value is not a Boolean!"));
        }
      }
    }
  }

  /**
   * 
   * New input data has arrived, that means some events can be triggered,
   * such as jumps, jetpack activation etc.
   * These should only be triggered in control updates, so we just store inputs here,
   * for further processing, when update is triggered.
   * 
   * @param input A queue of actions gathered in the last time frame.
   */
  public void newInput(LinkedList<InputTuple> input) {
    this.serverControlInput = input;

  }
}