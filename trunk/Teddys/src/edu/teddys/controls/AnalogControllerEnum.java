/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import com.jme3.input.KeyInput;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cm
 */
public enum AnalogControllerEnum {

  MOVE_LEFT(Arrays.asList(KeyInput.KEY_A, KeyInput.KEY_LEFT)),
  MOVE_RIGHT(Arrays.asList(KeyInput.KEY_D, KeyInput.KEY_RIGHT));
  private final List<Integer> keys;

  AnalogControllerEnum(List<Integer> inputList) {
    keys = inputList;
  }

  public List<Integer> getKeys() {
    return keys;
  }
}
