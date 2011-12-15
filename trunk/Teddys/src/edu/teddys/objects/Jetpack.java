/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects;

/**
 *
 * @author cm
 */
public class Jetpack {
  
  private Integer maxEnergy;
  private Integer currentEnergy;
  private boolean enabled = false;

  public Integer getCurrentEnergy() {
    return currentEnergy;
  }

  public void setCurrentEnergy(Integer currentEnergy) {
    this.currentEnergy = currentEnergy;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Integer getMaxEnergy() {
    return maxEnergy;
  }

  public void setMaxEnergy(Integer maxEnergy) {
    this.maxEnergy = maxEnergy;
  }
  
  
}
