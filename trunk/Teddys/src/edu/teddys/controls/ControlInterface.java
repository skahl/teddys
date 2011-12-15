/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import com.jme3.input.controls.Trigger;

/**
 *
 * @author cm
 */
public interface ControlInterface {
  public Trigger[] left();
  public Trigger[] right();
  public Trigger[] up();
  public Trigger[] down();
  public Trigger[] jump();
  public Trigger[] activate();
  public Trigger[] fire();
  public Trigger[] previousWeapon();
  public Trigger[] nextWeapon();
  public Trigger[] weapon1();
  public Trigger[] weapon2();
  public Trigger[] weapon3();
  public Trigger[] weapon4();
  public Trigger[] weapon5();
  public Trigger[] weapon6();
  public Trigger[] weapon7();
  public Trigger[] weapon8();
}
