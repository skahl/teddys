/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.Trigger;

/**
 *
 * @author cm
 */
public enum ActionControllerEnum {
  
  JETPACK(new Trigger[]{new KeyTrigger(KeyInput.KEY_SPACE)}),
  PREVIOUS_WEAPON(new Trigger[]{new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false)}),
  NEXT_WEAPON(new Trigger[]{new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true)});
  
  private final Trigger[] keys;

  private ActionControllerEnum(Trigger[] keysList) {
    keys = keysList;
  }

  public Trigger[] getKeys() {
    return keys;
  }
}
