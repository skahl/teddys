/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;

/**
 *
 * @author cm
 */
public enum AnalogControllerEnum {

  MOVE_LEFT(new Trigger[]{new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT)}),
  MOVE_RIGHT(new Trigger[]{new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT)});
  
  private final Trigger[] keys;

  AnalogControllerEnum(Trigger[] inputList) {
    keys = inputList;
  }

  public Trigger[] getKeys() {
    return keys;
  }
}
