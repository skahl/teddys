/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

/**
 *
 * @author cm
 */
public class SniperRifle implements Weapon {
  
  public String getName() {
    return "Sniper Rifle";
  }

  public float getBaseDamage() {
    return 1f;
  }

  public float getRange() {
    return 1f;
  }

  public float getAccuracy() {
    return .9f;
  }

  public float getFireRate() {
    return .4f;
  }

  public float getSpawningRate() {
    return .1f;
  }
}
