/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import com.jme3.network.serializing.Serializable;
import edu.teddys.effects.Effect;
import edu.teddys.effects.FloretsShot;
import edu.teddys.objects.player.Player;

/**
 *
 * @author cm
 */
@Serializable
public class Florets implements Weapon {
  
  transient FloretsShot floretsShot;
  Player player;
  
  public Florets(Player player) {
    this.player = player;
    floretsShot = new FloretsShot(this);
  }
  
  public Integer getPlayerID() {
    return player.getData().getId();
  }
  
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
