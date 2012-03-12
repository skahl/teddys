
package edu.teddys.effects;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * Stores custom particle options.
 * 
 * @author skahl
 */
public abstract class CustomParticle extends Geometry {
  // physics
  protected float rotateSpeed = 0f;
  protected boolean isFloating = false;
  protected float floatingSinusAmplitude = 1.0f;
  protected Vector3f initialVector = new Vector3f(1f, 0f, 0f);
  protected float timeToHalt = 0f;
  protected Vector3f gravityVector = new Vector3f(0f, -1f, 0f);
  
  /**
   * CustomParticle constructor. Texture size needs to be a power of two.
   * @param mat 
   */
  public CustomParticle(Material mat) {
    
    mesh = new Quad(0.1f, 0.1f);
    this.material = mat;
  }
  
  abstract void init();
  
  public void setInitialVector(Vector3f init) {
    initialVector = init;
  }
   
  public float getRotateSpeed() {
    return rotateSpeed;
  }
  
  public boolean isFloating() {
    return isFloating;
  }
  
  public float getFloatingSinusAmplitude() {
    return floatingSinusAmplitude;
  }
  
  public Vector3f getInitialVector() {
    return initialVector;
  }
  
  public float getTimeToHalt() {
    return timeToHalt;
  }
  
  public Vector3f getGravityVector() {
    return gravityVector;
  }
}
