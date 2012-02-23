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
public class Florets implements Weapon {
  
  public String getName() {
    return "Florets";
  }

  public float getBaseDamage() {
    return .5f;
  }

  public float getRange() {
    return .2f;
  }

  public float getAccuracy() {
    return .1f;
  }

  public float getFireRate() {
    return .7f;
  }

  public float getSpawningRate() {
    return .2f;
  }

  public Effect getEffect() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
