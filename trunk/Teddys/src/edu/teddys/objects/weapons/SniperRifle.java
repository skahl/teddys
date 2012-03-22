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
public class SniperRifle implements Weapon {
  
  Integer playerID;
  
  public SniperRifle(Integer playerID) {
    this.playerID = playerID;
  }
  
  public Integer getPlayerID() {
    return playerID;
  }
  
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

  public Effect getEffect() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
