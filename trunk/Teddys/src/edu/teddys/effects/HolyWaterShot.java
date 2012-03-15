
package edu.teddys.effects;

import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.states.Game;


/**
 * Implements the HolyWaterShot Effect. Triggering a splash of holy water. Water disappears on impact. 
 * 
 * @author skahl
 */
public class HolyWaterShot extends GhostControl implements Effect {
  
  // control variables
  boolean canShoot;
  
  // Effect attributes
  Node mother;
  HolyWaterParticle particle;
//  ParticleEmmitter
  ParticleCollisionBox partColBox;
  
  
  public HolyWaterShot() {
    
    // init control variables
    canShoot = true;
    
    // init effect attributes
    mother = new Node("Holy Water");
    particle = new HolyWaterParticle(mother.getName());
    partColBox = new ParticleCollisionBox(mother.getName(), particle);
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    partColBox.hideParticle(true);
    
    partColBox.getNode().addControl(this);
  }
  
  @Override
  public void trigger() {
    
    
    if(canShoot) {
      canShoot = false;
      
      Game.getInstance().getApp().enqueue(new AttachToNodeCallable(mother, partColBox.getNode()));
      
      if(particle.getVector().x > 0f) {
        partColBox.getNode().setLocalTranslation(0.8f, 0.1f, 0f);
      } else {
        partColBox.getNode().setLocalTranslation(-0.8f, 0.1f, 0f);
      }
      
      setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      canShoot = true;
      
      setEnabled(false);
      
      Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(mother, partColBox.getNode()));
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
