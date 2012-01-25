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

  private Integer client;
  private Integer damage;
  
  public ManMessageSendDamage() {
    super();
  }

  public ManMessageSendDamage(Integer client, Integer damage) {
    this();
    if (client == null || damage == null || damage <= 0) {
      throw new InstantiationError("Client or positive damage value must be specified!");
    }
    setClient(client);
    setDamage(damage);
  }

  public Integer getClient() {
    return client;
  }

  private void setClient(Integer client) {
    this.client = client;
  }

  public Integer getDamage() {
    return damage;
  }

  private void setDamage(Integer damage) {
    this.damage = damage;
  }
}
