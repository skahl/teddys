
package edu.teddys.effects;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * Stores custom particle options.
 * 
 * @author skahl
 */
public class CustomParticle extends Geometry {
  
  // physics
  protected float rotateSpeed = 0f;
  protected boolean isFloating = false;
  protected float floatingSinusAmplitude = 0.0f;
  protected Vector3f initialVector = new Vector3f(1f, 0f, 0f);
  protected float timeToHalt = 0f;
  protected Vector3f gravityVector = new Vector3f(0f, -1f, 0f);
    
  /**
   * Needs to specify the Geometry and Material, that will be displayed as a particle.
   */
  public CustomParticle(String name) {
    setName(name);
  }
  
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
  
  public float getQuadWidth() {
    return ((Quad)mesh).getWidth();
  }
  
  public float getQuadHeight() {
    return ((Quad)mesh).getHeight();
  }
}
