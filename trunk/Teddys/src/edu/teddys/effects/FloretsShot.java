
package edu.teddys.effects;

import com.jme3.bullet.control.GhostControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.objects.weapons.Florets;
import edu.teddys.states.Game;
import edu.teddys.timer.ClientTimer;
import edu.teddys.timer.ServerTimer;


/**
 * Implements the HolyWaterShot Effect. Triggering a splash of holy water. Water disappears on impact. 
 * 
 * @author skahl
 */
public class FloretsShot extends GhostControl implements Effect {
  Florets weapon;
  
  // control variables
  boolean canShoot;
  float tickCounter = 0f;
  
  // Effect attributes
  Node mother;
  FloretsParticle particle;
//  ParticleEmmitter
  ParticleCollisionBox partColBox;
  
  
  public FloretsShot(Florets weapon) {
    this.weapon = weapon;
    // init control variables
    canShoot = true;
    
    // init effect attributes
    mother = new Node("Florets");
    particle = new FloretsParticle(mother.getName());
    partColBox = new ParticleCollisionBox(mother.getName(), weapon, particle);
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    
    partColBox.getNode().addControl(this);
  }
  
  @Override
  public void update(float tpf) {
    super.update(tpf);
    
    if(partColBox.collidedPlayer()) {
      reset();
    } else {
      // movement
      tickCounter += tpf;
      Vector3f mov = new Vector3f(0f, FastMath.sin(tickCounter)*0.005f, 0f);
      partColBox.getNode().move(mov);
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
      if(particle.getVector().x > 0f) {
        loc.addLocal(0.8f, 0.2f, 0f);
      } else {
        loc.addLocal(-0.8f, 0.2f, 0f);
      }
      partColBox.getNode().setLocalTranslation(loc);
      
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
        
        // reset control variables
        tickCounter = 0f;
        
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
