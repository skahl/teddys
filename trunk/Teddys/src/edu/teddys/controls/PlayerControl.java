package edu.teddys.controls;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import edu.teddys.MegaLogger;
import edu.teddys.hud.HUDController;
import edu.teddys.input.ActionControllerEnum;
import edu.teddys.input.AnalogControllerEnum;
import edu.teddys.input.InputType;
import edu.teddys.input.InputTuple;
import edu.teddys.objects.player.Player;
import edu.teddys.objects.player.TeddyVisual;
import edu.teddys.objects.weapons.Rocket;
import edu.teddys.objects.weapons.Weapon;
import edu.teddys.states.Game;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Control class responsible for character controls and visuals, based on input.
 *
 * @author besient,skahl,cm
 */
public class PlayerControl extends CharacterControl {

  private Player player;
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
  private Vector3f left, right;
  // player control input from server
  private LinkedList<InputTuple> serverControlInput = new LinkedList<InputTuple>();
  private short controlTimer = 0;
  private short jetpackTimer = 0;
  private float weaponTimer = 0.0f;
  private short hudUpdateTimer = 0;
  // Weapon
  Weapon currentWeapon;

  /**
   * PlayerControl constructor. Sets physics properties.
   * 
   * @param player
   * @param collisionShape
   * @param stepHeight
   * @param vis 
   */
  public PlayerControl(Player player, CollisionShape collisionShape, float stepHeight, TeddyVisual vis) {
    super(collisionShape, stepHeight);

    this.player = player;
    visual = vis;
    serverControlInput = new LinkedList<InputTuple>();

    left = new Vector3f(-1, 0, 0);
    right = new Vector3f(1, 0, 0);

//    currentWeapon = new DeafNut(player);//new Florets(player);//new Rocket(player);//new HolyWater(player);//new HoneyBrew(player);//new StenGun(player);//
    currentWeapon = new Rocket(player);
//    currentWeapon = new Florets(player);
//    currentWeapon = new HolyWater(player);
//    currentWeapon = new HoneyBrew(player);
//    currentWeapon = new StenGun(player);

    Game.getInstance().addSpatial(visual.getNode(), currentWeapon.getEffect().getNode());
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

      //MegaLogger.getLogger().info("PHYSICSLOCATION: "+String.valueOf(this.getPhysicsLocation()));

      // check if weapon can fire again
      if (currentWeapon.getEffect().isTriggerable()) {

        // visual
        currentWeapon.getEffect().trigger();

//        Ray ray = new Ray(getPhysicsLocation(), currentWeapon.getEffect().getVector());
//        // I could do that!
//        //ray.setLimit(); ??? 
//        CollisionResults hits = new CollisionResults();     
//
//        Game.getInstance().getRootNode().collideWith(ray, hits);
//
//        // Check if a Box was hit, since Box are only used by Players...
//        if(hits.size() > 1) {// && hits.getCollision(2).getGeometry().getClass().equals(Box.class)) {
//          Geometry p = hits.getCollision(1).getGeometry();
//          String nodeName = p.getParent().getName();
//
//          // I found no other way than to compare a string... :(
//          if(p.getParent().getName().contains("player")) {
//
//            Player plHit = Player.getPlayerByNode(nodeName);
//            plHit.getPlayerVisual().die(); // play the die effect :D
//
//            // Now calculate the specific damage dependend from the current weapon
//            /* If there is no other reason than for checking the weapon's range,
//             * one could use ray.setLimit() to limit the intersection check distance
//             * 
//             */
//            float distance = hits.getCollision(1).getDistance();
//            MegaLogger.getLogger().debug("Distance: " + distance);
//            //Vector3f iPoint = hits.getCollision(1).getContactPoint();
//
//            // Assume we have a honey brew yet
//            Weapon w = new DeafNut();
//            RandomDataImpl rnd = new RandomDataImpl();
//            Double weaponDamage = Math.abs(rnd.nextGaussian(w.getBaseDamage(), GameSettings.DAMAGE_SIGMA));
//            Double weaponAccuracy = Math.abs(rnd.nextGaussian(w.getAccuracy(), GameSettings.DAMAGE_SIGMA));
//            Double damage = GameSettings.DAMAGE_MAX * weaponDamage * weaponAccuracy;
//            MegaLogger.getLogger().info(String.format("m=%f,b=%f,a=%f",
//                    GameSettings.DAMAGE_MAX,
//                    weaponDamage,
//                    weaponAccuracy));
//            int resDamage = (int) Math.ceil(damage);
//            String hitmsg = " Damage: " + resDamage;
//            ManMessageSendDamage dmgMsg = new ManMessageSendDamage(
//                    player.getData().getId(),
//                    plHit.getData().getId(),
//                    resDamage);
//
//            MegaLogger.getLogger().info("Hit: "+nodeName+hitmsg);
//            TeddyServer.getInstance().send(dmgMsg);
//          }
//        }
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
    //System.out.println("PlayerControl received event " + name);
    if (name.equals(ActionControllerEnum.NEXT_WEAPON.name())) {


      Class currentClassWeapon;
      Constructor currentContructor;

      try {
        currentWeapon.getEffect().reset();

        currentClassWeapon = Class.forName(player.getNextWeapon());
        currentContructor = currentClassWeapon.getConstructor(Player.class);
        currentWeapon = (Weapon) currentContructor.newInstance(player);
        //HUDController.getInstance().showWeapons();
        //HUDController.getInstance().nextWeapon();
        HUDController.getInstance().selectWeapon(player.getActiveWeapon());

        // after changing the weapon, attach the new weapon on the player's node

        Game.getInstance().addSpatial(visual.getNode(), currentWeapon.getEffect().getNode());
      } catch (InstantiationException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvocationTargetException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (NoSuchMethodException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SecurityException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      }





      MegaLogger.getLogger().debug("Weapon changed: " + player.getActiveWeapon());

    } else if (name.equals(ActionControllerEnum.PREVIOUS_WEAPON.name())) {
      Class currentClassWeapon;
      Constructor currentContructor;

      try {
        currentWeapon.getEffect().reset();

        currentClassWeapon = Class.forName(player.getPreviousWeapon());
        currentContructor = currentClassWeapon.getConstructor(Player.class);
        currentWeapon = (Weapon) currentContructor.newInstance(player);
        //HUDController.getInstance().showWeapons();
        //HUDController.getInstance().nextWeapon();
        HUDController.getInstance().selectWeapon(player.getActiveWeapon());


        Game.getInstance().addSpatial(visual.getNode(), currentWeapon.getEffect().getNode());
      } catch (InstantiationException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvocationTargetException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (NoSuchMethodException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SecurityException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(PlayerControl.class.getName()).log(Level.SEVERE, null, ex);
      }


    }
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
      visual.getJetpack().trigger();
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

    visual.getJetpack().reset();
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
  public void setScreenPositions(Vector2f cursorPos) {

    Vector3f playerPos = Game.getInstance().getApp().getCamera().getScreenCoordinates(player.getNode().getWorldTranslation());

    vectorPlayerToCursor = (cursorPos.subtract(new Vector2f(playerPos.x, playerPos.y))).normalizeLocal();
    currentWeapon.getEffect().setVector(new Vector3f(vectorPlayerToCursor.x, vectorPlayerToCursor.y, 0f));
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
    weaponTimer += tpf;
    if (weaponTimer > currentWeapon.getFireRate()) {
      currentWeapon.getEffect().reset();
      weaponTimer = 0.0f;
    }

    // increase HUD timer and act on it, if necessary
    hudUpdateTimer++;
    if (hudUpdateTimer > 4) {
      hudUpdateTimer = 0;

      // inform the visual representation of this player about the view vector
      visual.setViewVector(vectorPlayerToCursor);

      // update HUD
      HUDController.getInstance().setJetpackEnergy((int) currentEnergy);
    }

    InputTuple entry = null;

    synchronized (serverControlInput) {

      while (serverControlInput.size() > 0) {

        entry = serverControlInput.pop();
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
  public synchronized void newInput(LinkedList<InputTuple> input) {
    synchronized(serverControlInput) {
      try {
        serverControlInput.addAll(input);
      } catch(Exception ex) {
        // Sometimes a NUllPointerException is generated because of concurrency?
      }
    }
  }
}