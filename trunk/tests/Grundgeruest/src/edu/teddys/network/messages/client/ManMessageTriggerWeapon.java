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
  private Integer[] targets;

  public ManMessageTriggerWeapon(String weapon, Integer[] targets) {
    if (weapon == null) {
      throw new InstantiationError("Weapon not specified!");
    }
    setWeaponName(weapon);
    setTargets(targets);
  }
  
  public ManMessageTriggerWeapon() {
    super();
  }

  public Integer[] getTargets() {
    return targets;
  }

  private void setTargets(Integer[] clientIDs) {
    this.targets = clientIDs;
  }

  public String getWeaponName() {
    return weaponName;
  }

  private void setWeaponName(String weaponName) {
    this.weaponName = weaponName;
  }
}
