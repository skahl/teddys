/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.Effect;

/**
 *
 * @author cm
 */
public class DeafNut implements Weapon {
  
  public String getName() {
    return "Deaf Nut";
  }

  public float getBaseDamage() {
    return .1f;
  }

  public float getRange() {
    return 1f;
  }

  public float getAccuracy() {
    return .6f;
  }

  public float getFireRate() {
    return 1f;
  }

  public float getSpawningRate() {
    return .3f;
  }

  public Effect getEffect() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
