/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cm
 */
public class ControllerEvents {

  public static Map<String, List<Trigger>> getAllEvents() {

    Map<String, List<Trigger>> out = new HashMap<String, List<Trigger>>();

    for (ActionControllerEnum value : ActionControllerEnum.values()) {
      out.put(value.name(), Arrays.asList(value.getKeys()));
    }

    for (AnalogControllerEnum value : AnalogControllerEnum.values()) {
      out.put(value.name(), Arrays.asList(value.getKeys()));
    }
    return out;
  }
}
