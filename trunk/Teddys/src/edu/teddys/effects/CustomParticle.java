
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
  protected Vector3f gravity = Vector3f.ZERO;
  protected Vector3f initialVector = Vector3f.ZERO;
  protected float velocity = 0f;
    
  /**
   * Needs to specify the Geometry and Material, that will be displayed as a particle.
   */
  public CustomParticle(String name) {
    setName(name);
  }
  
  public Vector3f getGravity() {
    return gravity;
  }

  public Vector3f getVector() {
    return initialVector;
  }
  
  public void setVector(Vector3f vec) {
    initialVector = vec;
  }
  
  public void setVelocity(float vel) {
    velocity = vel;
  }
  
  public float getVelocity() {
    return velocity;
  }
  
  public float getQuadWidth() {
    return ((Quad)mesh).getWidth();
  }
  
  public float getQuadHeight() {
    return ((Quad)mesh).getHeight();
  }
}
