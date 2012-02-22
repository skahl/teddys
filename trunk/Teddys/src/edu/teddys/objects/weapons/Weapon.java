/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.weapons;

/**
 * 
 * Describes a weapon. All values range from 0 to 1.
 *
 * @author cm
 */
public interface Weapon {
  public String getName();
  public float getBaseDamage();
  public float getRange();
  public float getAccuracy();
  public float getFireRate();
  public float getSpawningRate();
}
