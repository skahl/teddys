/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.Effect;
import edu.teddys.effects.FloretsShot;

/**
 *
 * @author cm
 */
public class Florets implements Weapon {
  
  FloretsShot floretsShot = new FloretsShot();
  
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
    return .1f;
  }

  public float getSpawningRate() {
    return .2f;
  }

  public Effect getEffect() {
    return floretsShot;
  }
}
