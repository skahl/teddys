/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.math.Vector2f;
import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageTriggerWeapon extends NetworkMessageManipulation {

  private String weaponName = "";
  private Vector2f crosshair = new Vector2f();
  private Integer[] targets;
  
  public ManMessageTriggerWeapon() {
    super();
  }

  public ManMessageTriggerWeapon(String weapon, Vector2f crosshair, Integer[] targets) {
    this();
    if (weapon == null) {
      throw new InstantiationError("Weapon not specified!");
    }
    setWeaponName(weapon);
    setCrosshair(crosshair);
    setTargets(targets);
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

  public Vector2f getCrosshair() {
    return crosshair;
  }

  private void setCrosshair(Vector2f crosshair) {
    this.crosshair = crosshair;
  }
}
