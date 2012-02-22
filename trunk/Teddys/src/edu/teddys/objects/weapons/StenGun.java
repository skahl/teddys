/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

/**
 *
 * @author cm
 */
public class StenGun implements Weapon {
  
  public String getName() {
    return "Sten Gun";
  }

  public float getBaseDamage() {
    return .3f;
  }

  public float getRange() {
    return .6f;
  }

  public float getAccuracy() {
    return .7f;
  }

  public float getFireRate() {
    return 1f;
  }

  public float getSpawningRate() {
    return .05f;
  }
}
