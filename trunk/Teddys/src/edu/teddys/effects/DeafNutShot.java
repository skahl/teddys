
package edu.teddys.effects;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.states.Game;

/**
 * Implements the HoneyBrewShot Effect. Triggering a splash of deadly honey. 
 * Will create a pond of honey, deadly aswell.
 * 
 * @author skahl
 */
public class DeafNutShot extends RigidBodyControl implements Effect {
  
  // control variables
  boolean canShoot;
  
  // Effect attributes
  Node mother;
  DeafNutParticle particle;
  Explosion explosion;
  ParticleCollisionBox partColBox;
  
  
  public DeafNutShot() {
    // init control variables
    canShoot = true;
    
    // init effect attributes
    mother = new Node("Deaf Nut");
    // init explosion
    explosion = new Explosion(1.5f);
    
    particle = new DeafNutParticle(mother.getName());
    partColBox = new ParticleCollisionBox(mother.getName(), particle);
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    partColBox.getNode().addControl(this);
    
    setMass(1f);
    this.setDamping(0.5f, 0.9f);
  }
  
  @Override
  public void update(float tpf) {
    super.update(tpf);
    
    // TODO: Sowas in allen Waffen um Refreshrate nicht abwarten zu müssen?
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
      loc.addLocal(particle.getVector());
      
      setPhysicsLocation(loc);
      setPhysicsRotation(rot);
            
      // rotate the partColBox, so that the particle looks good
      partColBox.getNode().rotateUpTo(particle.getVector());
      partColBox.getNode().rotate(0f, 0f, -FastMath.HALF_PI);
      
      // linear force to apply
      Vector3f force = new Vector3f(particle.getVector());
      // apply velocity
      force = force.mult(particle.getVelocity());
      
      setLinearVelocity(force);
      
      setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      partColBox.scaleCollisionBox(4f);
      
      // position the explosion on the root node and explode
      explosion.trigger(getPhysicsLocation());
      
      canShoot = true;
      setEnabled(false);

      // remove the cube from the field
      Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(
              Game.getInstance().getRootNode(), partColBox.getNode()));

      // reset rigidBody physics
      partColBox.resetCollisionBoxScale();

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
