package edu.teddys.controls;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
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
import com.jme3.scene.shape.Line;
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

/**
 *
 * @author besient
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

  public PlayerControl(Spatial player, CollisionShape collisionShape, float stepHeight, TeddyVisual vis) {
    super(collisionShape, stepHeight);
    setPhysicsLocation(player.getWorldTranslation());
    player.addControl(this);

    visual = vis;
    serverControlInput = new LinkedList<InputTuple>();

    left = new Vector3f(-1, 0, 0);
    right = new Vector3f(1, 0, 0);

  }

  public void registerWithInput(InputManager input) {
    this.input = input;

    Map<String, List<Trigger>> map = ControllerEvents.getAllEvents();

    input.addListener(this, map.keySet().toArray(new String[map.keySet().size()]));
  }

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
      visual.getBox().updateBound();

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

      // shoot
      //Node playerInstances = Game.getInstance().getRootNode();

      //TODO the damage should only be calculated if a particle has been emitted

      // Get all players except for the current one on the server
      Node playerInstances = Player.getPlayerTree(
              Player.getInstance(Player.LOCAL_PLAYER).getNode().getName()
              );

      CollisionResults hits = new CollisionResults();

      ////////////////////
//      Line shot = new Line(getPhysicsLocation(), getPhysicsLocation().add(visual.getWeapon().getVector()));
//      Geometry gs = new Geometry("shot", shot);
//      Material red = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//      red.setColor("Color", ColorRGBA.Red);
//      gs.setMaterial(red);
//      Game.getInstance().getRootNode().attachChild(gs);
      ////////////////////
      
      
      // Kopf->Tisch ;)
      Ray ray = new Ray(getPhysicsLocation(), getPhysicsLocation().add(visual.getWeapon().getVector()));

      int numHits = playerInstances.collideWith(ray, hits);

      for (int i = 0; i < numHits; i++) {
        String nodeName = hits.getCollision(i).getGeometry().getName();
        String hitmsg = nodeName + " was hit!";
        //TODO Now calculate the specific damage dependend from the current weapon
        float distance = hits.getCollision(i).getDistance();
        MegaLogger.getLogger().debug("Distance: " + distance);
        Vector3f iPoint = hits.getCollision(i).getContactPoint();
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
        hitmsg += " Damage: " + resDamage;
        ManMessageSendDamage dmgMsg = new ManMessageSendDamage(
                Player.getPlayerByNode(nodeName).getData().getId(), resDamage);
        //TODO this should only be done by the server instance!
        TeddyServer.getInstance().send(dmgMsg);
        MegaLogger.getLogger().info(new Throwable(hitmsg));
      }
    }

    if (name.contains(AnalogControllerEnum.JETPACK.name())) {

      jetpackTimer = 0;

      if (!jetpackActive) {
        startJetpack();
      }
    }

  }

  public void onAction(String name, boolean isPressed, float tpf) {
//    if (name.equals(ActionControllerEnum.JETPACK.name())) {
//      if (!jetpackActive && isPressed) {
//        startJetpack();
//      } else {
//        stopJetpack();
//      }
//    }
  }

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

  private void stopJetpack() {
    setGravity(oldGravity);
    jetpackActive = false;

    visual.getJetpack().setEnabled(false);
  }

  @Override
  public void setGravity(float value) {
    super.setGravity(value);
  }

  public void setScreenPositions(Vector2f playerPos, Vector2f cursorPos) {
    vectorPlayerToCursor = (cursorPos.subtract(playerPos)).normalizeLocal();
    visual.getWeapon().setVector(new Vector3f(vectorPlayerToCursor.x, vectorPlayerToCursor.y, getPhysicsLocation().getZ()));
  }

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