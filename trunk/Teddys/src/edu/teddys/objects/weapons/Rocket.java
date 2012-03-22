/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.Effect;
import edu.teddys.effects.RocketShot;

/**
 *
 * @author besient
 */
public class Rocket implements Weapon {
  
  RocketShot rocketShot;
  Integer playerID;
  
  public Rocket(Integer playerID) {
    this.playerID = playerID;
    rocketShot = new RocketShot(this);
  }
  
  public Integer getPlayerID() {
    return playerID;
  }
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
    return 1f;
  }

  public float getSpawningRate() {
    return .2f;
  }

  public Effect getEffect() {
    return rocketShot;
  }
    
}
