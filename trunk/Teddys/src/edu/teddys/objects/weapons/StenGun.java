/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.Effect;
import edu.teddys.effects.StenGunShot;

/**
 *
 * @author cm
 */
public class StenGun implements Weapon {
  
  StenGunShot shot;
  
  public StenGun() {
    shot = new StenGunShot();
  }
  
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

  public Effect getEffect() {
    return shot;
  }
}
