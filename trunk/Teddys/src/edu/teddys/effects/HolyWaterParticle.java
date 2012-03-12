
package edu.teddys.effects;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;

/**
 * Implements HolyWater Particle properties
 * 
 * @author skahl
 */
public class HolyWaterParticle extends CustomParticle {

  public HolyWaterParticle(Material mat) {
    super(mat);
    
    
  }
  
  @Override
  void init() {
    this.rotateSpeed = 0.1f;
    this.isFloating = false;
    this.floatingSinusAmplitude = 0f;
    this.gravityVector = new Vector3f(0f, -1f, 0f);
    this.initialVector = new Vector3f(1f, 0f, 0f);
    this.timeToHalt = 0f;
    
    
  }
  
}
