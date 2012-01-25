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
  
  private Integer affectedClient;
  private String effectName;
  
  public ManMessageTriggerEffect() {
    super();
  }
  
  public ManMessageTriggerEffect(Integer client, String effect) {
    this();
    if(client == null || client == 0 || effect == null) {
      throw new InstantiationError("Client or effect name must be specified!");
    }
    setAffectedClient(client);
    setEffectName(effect);
  }

  public Integer getAffectedClient() {
    return affectedClient;
  }

  private void setAffectedClient(Integer affectedClient) {
    this.affectedClient = affectedClient;
  }

  public String getEffectName() {
    return effectName;
  }

  private void setEffectName(String effectName) {
    this.effectName = effectName;
  }
}
