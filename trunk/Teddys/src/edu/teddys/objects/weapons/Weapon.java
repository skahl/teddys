/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

import edu.teddys.effects.Effect;


/**
 * 
 * Describes a weapon.
 *
 * @author cm
 */
public interface Weapon {
  public String getName();
  public Effect getEffect();
  public float getBaseDamage();
  public float getRange();
  public float getAccuracy();
  public float getFireRate(); // times per second
  public float getSpawningRate();
  public Integer getPlayerID();
}
