
package edu.teddys.effects;

import com.jme3.bullet.control.GhostControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.MegaLogger;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.objects.weapons.StenGun;
import edu.teddys.states.Game;

/**
 * Implements the RocketShot Effect. Triggering an unstoppable rocket. Smoke appears. 
 * 
 * @author skahl
 */
public class StenGunShot extends GhostControl implements Effect {
  StenGun weapon;
  // control variables
  boolean canShoot;
  
  // Effect attributes
  Node mother;
  StenGunParticle particle;
  Vector3f triggerVector;
//  ParticleEmmitter
  ParticleCollisionBox partColBox;
  
  
  
  public StenGunShot(StenGun weapon) {
    this.weapon = weapon;
    // init control variables
    canShoot = true;
    triggerVector = Vector3f.UNIT_X;
    
    // init effect attributes
    mother = new Node("Sten Gun");
    particle = new StenGunParticle(mother.getName());
    partColBox = new ParticleCollisionBox(mother.getName(), weapon, particle);
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    partColBox.getNode().addControl(this);
  }
  
  @Override
  public void update(float tpf) {
    super.update(tpf);
    
    if(partColBox.collided()) {
      if(partColBox.collidedPlayer()) {
        
      }
      
      reset();
    } else {
      
      // linear force to apply
      Vector3f force = triggerVector;
      // apply velocity
      force = force.mult(particle.getVelocity());
      
      force = force.mult(tpf);
      
      partColBox.getNode().move(force);
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
      loc.addLocal(particle.getVector());
      
      partColBox.getNode().setLocalTranslation(loc);
      
      // store the vector at trigger time, so that the gunshot can fly uncontrolled
      triggerVector = new Vector3f(particle.getVector());
      
      // rotate the partColBox, so that the particle looks good
      partColBox.getNode().rotateUpTo(particle.getVector());
      partColBox.getNode().rotate(0f, 0f, -FastMath.HALF_PI);
      
      setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      
      canShoot = true;
      setEnabled(false);

      // remove the cube from the field
      Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(
              Game.getInstance().getRootNode(), partColBox.getNode()));
      
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
