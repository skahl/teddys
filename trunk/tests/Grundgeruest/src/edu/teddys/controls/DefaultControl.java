/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;

/**
 *
 * @author cm
 */
public class DefaultControl implements ControlInterface {

  public Trigger[] left() {
    return new Trigger[]{new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A)};
  }

  public Trigger[] right() {
    return new Trigger[]{new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D)};
  }

  public Trigger[] up() {
    return new Trigger[]{new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W)};
  }

  public Trigger[] down() {
    return new Trigger[]{new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S)};
  }

  public Trigger[] jump() {
    return new Trigger[]{new KeyTrigger(KeyInput.KEY_SPACE)};
  }

  public Trigger[] activate() {
    return new Trigger[]{new KeyTrigger(KeyInput.KEY_E)};
  }

  public Trigger[] fire() {
    return new Trigger[]{new MouseButtonTrigger(MouseInput.BUTTON_LEFT)};
  }

  public Trigger[] previousWeapon() {
    return null;
  }

  public Trigger[] nextWeapon() {
    return new Trigger[]{new MouseButtonTrigger(MouseInput.AXIS_WHEEL)};
  }

  public Trigger[] weapon1() {
    return new Trigger[]{new KeyTrigger(KeyInput.KEY_1)};
  }

  public Trigger[] weapon2() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Trigger[] weapon3() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Trigger[] weapon4() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Trigger[] weapon5() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Trigger[] weapon6() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Trigger[] weapon7() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Trigger[] weapon8() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
