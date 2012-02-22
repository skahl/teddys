/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

/**
 *
 * @author cm
 */
public class HolyWater implements Weapon {
  
  public String getName() {
    return "Holy Water";
  }

  public float getBaseDamage() {
    return 1f;
  }

  public float getRange() {
    return .1f;
  }

  public float getAccuracy() {
    return 1f;
  }

  public float getFireRate() {
    return .8f;
  }

  public float getSpawningRate() {
    return .1f;
  }
}
