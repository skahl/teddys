/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageTriggerWeapon extends NetworkMessageManipulation {

  private String weaponName;
  private Integer[] clientIDs;

  public ManMessageTriggerWeapon(String weapon, Integer[] clients) {
    if (weapon == null) {
      throw new InstantiationError("Weapon not specified!");
    }
    setWeaponName(weapon);
    setClientIDs(clients);
  }

  public Integer[] getClientIDs() {
    return clientIDs;
  }

  private void setClientIDs(Integer[] clientIDs) {
    this.clientIDs = clientIDs;
  }

  public String getWeaponName() {
    return weaponName;
  }

  private void setWeaponName(String weaponName) {
    this.weaponName = weaponName;
  }
}
