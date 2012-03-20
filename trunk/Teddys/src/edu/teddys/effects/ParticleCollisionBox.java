
package edu.teddys.effects;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import edu.teddys.controls.GeometryCollisionListener;
import edu.teddys.states.Game;

/**
 * Initialized with a given Particle, the box will be used to cheaply detect
 * collisions with that particle.
 * 
 * @author skahl
 */
public class ParticleCollisionBox {
  private Node particleNode;
  private Box box;
  private Geometry colBox;
  private CustomParticle particle;
  private GeometryCollisionListener colListener;
  private BoxCollisionShape csBox;
  
  /**
   * Creates a Box Geometry and a fitting BoxCollisionShape for the given CustomParticle.
   * Automatically attaches CustomParticle and Box Geometry to the given Node.
   * 
   * @param node
   * @param p 
   */
  public ParticleCollisionBox(String name, CustomParticle p) {
    particle = p;
    particleNode = new Node(name);
    
    box = new Box(particle.getQuadWidth()/2f, particle.getQuadHeight()/2f, 0.4f);
    
    colBox = new Geometry(name, box);
    
    Material blue = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    blue.setColor("Color", ColorRGBA.Blue);
    colBox.setMaterial(blue);
    colBox.setCullHint(Spatial.CullHint.Always); // collision box is invisible
    
    csBox = new BoxCollisionShape(new Vector3f(box.xExtent, box.yExtent, box.zExtent));
    
    colListener = new GeometryCollisionListener(particleNode);
    
    // attach particle and collision box to node
    particleNode.attachChild(particle);
    particleNode.attachChild(colBox);
    
    // move the collision cube to contain the particle
    particle.setLocalTranslation(-particle.getQuadWidth()/2f, -particle.getQuadHeight()/2f, 0f);
    
  }
  
  public BoxCollisionShape getCollisionShape() {
    return csBox;
  }
  
  public Node getNode() {
    return particleNode;
  }
  
  public boolean collided() {
    return colListener.collided();
  }
  
  public boolean collidedPlayer() {
    return colListener.collidedPlayer();
  }
  
  public void setEnabled(boolean enable) {
    colListener.setListen(enable);
  }
  
  public void hideParticle(boolean hide) {
    if(hide) {
      particle.setCullHint(Spatial.CullHint.Always);
    } else {
      particle.setCullHint(Spatial.CullHint.Inherit);
    }
  }
}
