/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.input.controls.Trigger;
import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cm
 */
@Serializable
public class ManControllerInput extends NetworkMessageManipulation {

  List<Trigger> triggers = new ArrayList<Trigger>();

  public ManControllerInput() {
    super();
  }

  public ManControllerInput(List<Trigger> triggers) {
    this.triggers = triggers;
  }

  public List<Trigger> getTriggers() {
    return triggers;
  }

  public void setTriggers(List<Trigger> triggers) {
    this.triggers = triggers;
  }
}
