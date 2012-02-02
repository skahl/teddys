/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageTriggerEffect extends NetworkMessageManipulation {
  
  private String effectName;
  
  public ManMessageTriggerEffect() {
    super();
  }
  
  public ManMessageTriggerEffect(Integer clientID, String effect) {
    super(new Integer[]{clientID});
    if(clientID == null || clientID == 0 || effect == null) {
      throw new InstantiationError("Client or effect name must be specified!");
    }
    setEffectName(effect);
  }

  public String getEffectName() {
    return effectName;
  }

  private void setEffectName(String effectName) {
    this.effectName = effectName;
  }
}
