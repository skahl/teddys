
package edu.teddys.effects;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.states.Game;

/**
 * Implements the ItemBoxSpawn Effect. Triggering the appearance of an item box. 
 * 
 * @author skahl
 */
public class ItemBoxSpawn extends RigidBodyControl implements Effect {
  
  // control variables
  boolean canShoot;
  
  // Effect attributes
  Node mother;
  ItemBoxParticle particle;
//  ParticleEmmitter
  ParticleCollisionBox partColBox;
  
  
  public ItemBoxSpawn() {
    // init control variables
    canShoot = true;
    
    // init effect attributes
    mother = new Node("Item Box");
    particle = new ItemBoxParticle(mother.getName());
    partColBox = new ParticleCollisionBox(mother.getName(), null, particle);
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    partColBox.getNode().addControl(this);
    
    setMass(1f);
    this.setDamping(0.9f, 0.9f);
  }
  
  @Override
  public void update(float tpf) {
    super.update(tpf);
    
    if(partColBox.collidedPlayer()) {
      reset();
    }
  }

  
  @Override
  public void trigger() {
        
    if(canShoot) {
      canShoot = false;
      
      // attach the collision cube to the root node, not the player node!
      Game.getInstance().getApp().enqueue(new AttachToNodeCallable(
              Game.getInstance().getRootNode(), partColBox.getNode()));
      
      Vector3f loc = mother.getWorldTranslation();
      Quaternion rot = mother.getLocalRotation();
      setPhysicsLocation(loc);
      setPhysicsRotation(rot);
      
      setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      
      if(partColBox.collidedPlayer()) {
        canShoot = true;
        setEnabled(false);

        // remove the cube from the field
        Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(
                Game.getInstance().getRootNode(), partColBox.getNode()));
        
        // reset rigidBody physics
        
      }
    }
  }
  
  @Override
  public void setEnabled(boolean en) { 
    super.setEnabled(en);
    
    // enable collision listening
    partColBox.setEnabled(en);
  }
  
  @Override
  public boolean isEnabled() {
    return super.isEnabled();
  }

  public Node getNode() {
    return mother;
  }

  public Vector3f getVector() {
    return particle.getVector();
  }

  public void setVector(Vector3f vector) {
    particle.setVector(vector);
  }

  public boolean isTriggerable() {
    return canShoot;
  }
}
