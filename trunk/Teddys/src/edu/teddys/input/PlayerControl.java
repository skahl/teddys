package edu.teddys.input;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import edu.teddys.hud.HUDController;

/**
 *
 * @author besient
 */
public class PlayerControl extends CharacterControl implements AnalogListener, ActionListener {

  /**
   * The enum for the event types.
   */
  private enum MAPPING_CONTROL {
    MOVE_LEFT, MOVE_RIGHT, JUMP, JETPACK
  }
  private float moveSpeed = 0.02f;
  private float totalEnergy, currentEnergy;
  private boolean jetpackActive;
  private InputManager input;
  private Vector3f vel;
  private static final Float energyChargeRate = 25f;
  private static final Float energyDischargeRate = 75f;

  public PlayerControl(Spatial player, CollisionShape collisionShape, float stepHeight) {
    super(collisionShape, stepHeight);
    setPhysicsLocation(player.getWorldTranslation());
//        setFallSpeed(1f);
//        setJumpSpeed(1f);
//        setGravity(1f);
    player.addControl(this);


    totalEnergy = 100;
    currentEnergy = totalEnergy;

  }

  public void registerWithInput(InputManager input) {
    this.input = input;

    input.addMapping(MAPPING_CONTROL.MOVE_LEFT.name(), new KeyTrigger(KeyInput.KEY_A));
    input.addMapping(MAPPING_CONTROL.MOVE_RIGHT.name(), new KeyTrigger(KeyInput.KEY_D));
    input.addMapping(MAPPING_CONTROL.JUMP.name(), new KeyTrigger(KeyInput.KEY_SPACE));
    input.addMapping(MAPPING_CONTROL.JETPACK.name(), new KeyTrigger(KeyInput.KEY_LCONTROL));
    input.addMapping(MAPPING_CONTROL.JETPACK.name(), new KeyTrigger(KeyInput.KEY_RCONTROL));

    input.addListener(this, new String[]{MAPPING_CONTROL.MOVE_LEFT.name(),
              MAPPING_CONTROL.MOVE_RIGHT.name(),
              MAPPING_CONTROL.JUMP.name(),
              MAPPING_CONTROL.JETPACK.name()});
  }

  public void onAnalog(String name, float value, float tpf) {

    if (name.equals(MAPPING_CONTROL.MOVE_LEFT.name())) {
      vel = new Vector3f(-1, 0, 0);
      vel.multLocal(moveSpeed);
      warp(getPhysicsLocation().add(vel));
    } else if (name.equals(MAPPING_CONTROL.MOVE_RIGHT.name())) {
      vel = new Vector3f(1, 0, 0);
      vel.multLocal(moveSpeed);
      warp(getPhysicsLocation().add(vel));
    } else if (name.equals(MAPPING_CONTROL.JETPACK.name())) {
      jump();
    }

  }

  public void onAction(String name, boolean isPressed, float tpf) {
    if (name.equals(MAPPING_CONTROL.JETPACK.name())) {
      if (!jetpackActive && isPressed) {
        startJetpack();
      } else {
        stopJetpack();
      }
    }
  }

  private void startJetpack() {
    if (currentEnergy > 0f) {
      setGravity(-4f);
      jetpackActive = true;
    } else {
      stopJetpack();
    }
  }

  private void stopJetpack() {
    setGravity(4f);
    jetpackActive = false;
  }

  @Override
  public void update(float tpf) {
    super.update(tpf);
    if (jetpackActive) {
      if (currentEnergy > 0) {
        currentEnergy -= energyDischargeRate * tpf;
      } else {
        stopJetpack();
      }
    } else {
      if (currentEnergy < totalEnergy) {
        currentEnergy += energyChargeRate * tpf;
      }
    }

    HUDController.getInstance().setJetpackEnergy((int) currentEnergy);
  }
}