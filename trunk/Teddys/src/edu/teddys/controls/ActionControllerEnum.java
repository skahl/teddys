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
public enum ActionControllerEnum {
  JETPACK(Arrays.asList(KeyInput.KEY_SPACE));
  
  private final List<Integer> keys;

  private ActionControllerEnum(List<Integer> keysList) {
    keys = keysList;
  }

  public List<Integer> getKeys() {
    return keys;
  }
}
