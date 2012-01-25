package edu.teddys.input;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import edu.teddys.timer.ClientTimer;
import java.util.AbstractMap.SimpleEntry;

public class ControllerInputListener implements AnalogListener, ActionListener {

  public void onAnalog(String name, float value, float tpf) {
    ClientTimer.input.addLast(new SimpleEntry<String, Object>(name, value));
  }

  public void onAction(String name, boolean isPressed, float tpf) {
    ClientTimer.input.addLast(new SimpleEntry<String, Object>(name, isPressed));
  }

}