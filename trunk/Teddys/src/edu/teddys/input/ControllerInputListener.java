package edu.teddys.input;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector2f;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.client.ManMessageTriggerWeapon;
import edu.teddys.timer.ClientTimer;

public class ControllerInputListener implements AnalogListener, ActionListener {

  private static ControllerInputListener instance;

  public void onAnalog(String name, float value, float tpf) {
    Vector2f pos = Cursor.getInstance().getPosition();
    ClientTimer.input.add(new InputTuple(InputType.Analog, name, value, tpf, pos.x, pos.y));
    if(name.equals(AnalogControllerEnum.WEAPON.name())) {
      // send a trigger weapon message
      ManMessageTriggerWeapon weapon = new ManMessageTriggerWeapon(pos);
      TeddyClient.getInstance().send(weapon);
    }
  }

  public void onAction(String name, boolean isPressed, float tpf) {
    Vector2f pos = Cursor.getInstance().getPosition();
    ClientTimer.input.add(new InputTuple(InputType.Action, name, isPressed, tpf, pos.x, pos.y));
  }

  public static ControllerInputListener getInstance() {
    if (instance == null) {
      instance = new ControllerInputListener();
      MegaLogger.getLogger().debug("ControllerInputListener instance created.");
    }
    return instance;
  }

  protected ControllerInputListener() {
  }
}