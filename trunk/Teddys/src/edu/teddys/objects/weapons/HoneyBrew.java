/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.Effect;
import edu.teddys.effects.HoneyBrewShot;
import edu.teddys.objects.player.Player;

/**
 *
 * @author cm
 */
public class HoneyBrew implements Weapon {
  
  HoneyBrewShot honeyBrew;
  Player player;
  
  public HoneyBrew(Player player) {
    this.player = player;
    honeyBrew = new HoneyBrewShot(this);
  }
  
  public Integer getPlayerID() {
    return player.getData().getId();
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
    return 1f;
  }

  public float getSpawningRate() {
    return .4f;
  }

  public Effect getEffect() {
    return honeyBrew;
  }
}
