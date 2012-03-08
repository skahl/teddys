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
public class ManMessageSendDamage extends NetworkMessageManipulation {

  private Integer source;
  private Integer damage;
  
  public ManMessageSendDamage() {
    super();
  }

  public ManMessageSendDamage(Integer source, Integer clientID, Integer damage) {
    super(new Integer[]{clientID});
    this.source = source;
    if (clientID == null || damage == null || damage <= 0) {
      throw new InstantiationError("Client or positive damage value must be specified!");
    }
    setDamage(damage);
  }

  public ManMessageSendDamage(Integer source, Integer[] clientIDs, Integer damage) {
    super(clientIDs);
    this.source = source;
    if (clientIDs == null || clientIDs.length < 1 || damage == null || damage <= 0) {
      throw new InstantiationError("Client or positive damage value must be specified!");
    }
    setDamage(damage);
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Integer getDamage() {
    return damage;
  }

  private void setDamage(Integer damage) {
    this.damage = damage;
  }
}
