/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

/**
 *
 * @author cm
 */
public class HoneyBrew implements Weapon {
  
  public String getName() {
    return "Honey Brew";
  }

  public float getBaseDamage() {
    return .1f;
  }

  public float getRange() {
    return .1f;
  }

  public float getAccuracy() {
    return .1f;
  }

  public float getFireRate() {
    return .1f;
  }

  public float getSpawningRate() {
    return .4f;
  }
}
