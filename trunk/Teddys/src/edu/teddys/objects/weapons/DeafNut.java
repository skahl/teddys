/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.DeafNutShot;
import edu.teddys.effects.Effect;
import edu.teddys.objects.player.Player;

/**
 *
 * @author cm
 */
public class DeafNut implements Weapon {
  
  DeafNutShot deafNutShot;
  Player player;
  
  public DeafNut(Player player) {
    this.player = player;
    deafNutShot = new DeafNutShot(this);
  }
  
  public Integer getPlayerID() {
    return player.getData().getId();
  }
  
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
    return 5f;
  }

  public float getSpawningRate() {
    return .3f;
  }

  public Effect getEffect() {
    return deafNutShot;
  }
}
