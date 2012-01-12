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

    MOVE_LEFT, MOVE_RIGHT, JETPACK
  }
  private float moveSpeed = 1f;
  private boolean jetpackActive;
  private float jetpackDischargeRate = 75f;
  private float jetpackChargeRate = 25f;
  private float totalJetpackEnergy = 100f;
  private float currentEnergy = totalJetpackEnergy;
  private float oldGravity = 4f;
  private InputManager input;
  private Vector3f vel, left, right;

  public PlayerControl(Spatial player, CollisionShape collisionShape, float stepHeight) {
    super(collisionShape, stepHeight);
    setPhysicsLocation(player.getWorldTranslation());
    player.addControl(this);

    left = new Vector3f(-1, 0, 0);
    right = new Vector3f(1, 0, 0);
  }

  public void registerWithInput(InputManager input) {
    this.input = input;

    input.addMapping(MAPPING_CONTROL.MOVE_LEFT.name(), new KeyTrigger(KeyInput.KEY_A));
    input.addMapping(MAPPING_CONTROL.MOVE_RIGHT.name(), new KeyTrigger(KeyInput.KEY_D));
    input.addMapping(MAPPING_CONTROL.JETPACK.name(), new KeyTrigger(KeyInput.KEY_SPACE));

    input.addListener(this, new String[]{MAPPING_CONTROL.MOVE_LEFT.name(),
              MAPPING_CONTROL.MOVE_RIGHT.name(),
              MAPPING_CONTROL.JETPACK.name()});
  }

  public void onAnalog(String name, float value, float tpf) {

    if (name.equals(MAPPING_CONTROL.MOVE_LEFT.name())) {
      vel = left.mult(moveSpeed * tpf);
      warp(getPhysicsLocation().add(vel));
    } else if (name.equals(MAPPING_CONTROL.MOVE_RIGHT.name())) {
      vel = right.mult(moveSpeed * tpf);
      warp(getPhysicsLocation().add(vel));
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
      oldGravity = getGravity();
      setGravity(-getGravity());
      jetpackActive = true;
    } else {
      stopJetpack();
    }
  }

  private void stopJetpack() {
    setGravity(oldGravity);
    jetpackActive = false;
  }

  @Override
  public void setGravity(float value) {
    super.setGravity(value);
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

    HUDController.getInstance().setJetpackEnergy((int) currentEnergy);
  }
}