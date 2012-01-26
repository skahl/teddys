package edu.teddys.input;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import edu.teddys.MegaLogger;
import edu.teddys.hud.HUDController;
import java.util.List;
import java.util.Map;

/**
 *
 * @author besient
 */
public class PlayerControl extends CharacterControl implements AnalogListener, ActionListener {

  private float moveSpeed = 2f;
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

    Map<String, List<Trigger>> map = ControllerEvents.getAllEvents();

    input.addListener(this, map.keySet().toArray(new String[map.keySet().size()]));
  }

  public void onAnalog(String name, float value, float tpf) {
    if (name.equals(AnalogControllerEnum.MOVE_LEFT.name())) {
      vel = left.mult(moveSpeed * tpf);
      warp(getPhysicsLocation().add(vel));
    } else if (name.equals(AnalogControllerEnum.MOVE_RIGHT.name())) {
      vel = right.mult(moveSpeed * tpf);
      warp(getPhysicsLocation().add(vel));
    }

  }

  public void onAction(String name, boolean isPressed, float tpf) {
    if (name.equals(ActionControllerEnum.JETPACK.name())) {
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