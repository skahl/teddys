package edu.teddys.input;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import edu.teddys.MegaLogger;
import edu.teddys.timer.ClientTimer;

public class ControllerInputListener implements AnalogListener, ActionListener {

  private static ControllerInputListener instance;

  public void onAnalog(String name, float value, float tpf) {
    ClientTimer.input.add(new SimpleTriple(InputType.Analog, name, value, tpf));
  }

  public void onAction(String name, boolean isPressed, float tpf) {
    ClientTimer.input.add(new SimpleTriple(InputType.Action, name, isPressed, tpf));
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