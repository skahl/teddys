/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.player;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author cm
 */
@Serializable
public class Jetpack {
  
  private Integer maxEnergy = 100;
  private Integer currentEnergy = 100;
  private Boolean enabled = false;
  
  public Jetpack() {
    
  }

  public Integer getCurrentEnergy() {
    return currentEnergy;
  }

  public void setCurrentEnergy(Integer currentEnergy) {
    this.currentEnergy = currentEnergy;
  }

  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Integer getMaxEnergy() {
    return maxEnergy;
  }

  public void setMaxEnergy(Integer maxEnergy) {
    this.maxEnergy = maxEnergy;
  }
  
  
}
