
package edu.teddys.effects;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Describes a visible effect. Most often implementing a Particle Emitter.
 * Must hold a Node.
 * 
 * @author skahl
 */
public interface Effect {
  public Node getNode();
  public Vector3f getVector();
  public void setVector(Vector3f vector);
  public boolean isTriggerable();
  public void trigger();
  public void reset();
}
