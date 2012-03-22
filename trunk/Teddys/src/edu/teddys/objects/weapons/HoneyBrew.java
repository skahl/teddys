/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.Effect;
import edu.teddys.effects.HoneyBrewShot;

/**
 *
 * @author cm
 */
public class HoneyBrew implements Weapon {
  
  HoneyBrewShot honeyBrew;
  Integer playerID;
  
  public HoneyBrew(Integer playerID) {
    this.playerID = playerID;
    honeyBrew = new HoneyBrewShot(this);
  }
  
  public Integer getPlayerID() {
    return playerID;
  }
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

  public Effect getEffect() {
    return honeyBrew;
  }
}
