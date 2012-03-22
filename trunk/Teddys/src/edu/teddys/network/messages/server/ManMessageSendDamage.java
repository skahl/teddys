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

  private Integer source = new Integer(0);
  private Integer damagedTeddy = new Integer(0);
  private Integer damage = new Integer(0);
  
  public ManMessageSendDamage() {
    super();
  }

  public ManMessageSendDamage(Integer source, Integer damagedTeddy, Integer damage) {
    super();
    this.source = source;
    this.damagedTeddy = damagedTeddy;
    if (damagedTeddy == null || damage == null || damage <= 0) {
      throw new InstantiationError("Teddy ID is null or positive damage value must be specified!");
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

  public Integer getDamagedTeddy() {
    return damagedTeddy;
  }

  public void setDamagedTeddy(Integer damagedTeddy) {
    this.damagedTeddy = damagedTeddy;
  }
}
