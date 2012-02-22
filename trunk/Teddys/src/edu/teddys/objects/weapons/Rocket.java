/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

/**
 *
 * @author besient
 */
public class Rocket extends Projectile implements Weapon {

  public String getName() {
    return "Teddy Rocket";
  }

  public float getBaseDamage() {
    return 1f;
  }

  public float getRange() {
    return 1f;
  }

  public float getAccuracy() {
    return .8f;
  }

  public float getFireRate() {
    return .4f;
  }

  public float getSpawningRate() {
    return .2f;
  }
    
}
