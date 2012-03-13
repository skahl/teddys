
package edu.teddys.effects;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import edu.teddys.MegaLogger;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.controls.CollidableParticleControl;
import edu.teddys.states.Game;

/**
 * Implements the HolyWaterShot Effect. Triggering a splash of holy water. Water disappears on impact. 
 * 
 * @author skahl
 */
public class HoneyBrewShot extends GunShot {
  Box box;
  Node collisionNode;
  CollidableParticleControl partControl;  
  
  public HoneyBrewShot() {
    this.init("Honey Brew", Game.getInstance().getAssetManager(), "Textures/Effects/plush/plush.png");
    
    // set particle emitter stuff
    
    
    
    // set collision stuff
    
    partControl = new CollidableParticleControl(getNode().getName(), 
            new HoneyBrewParticle(getNode().getName()));
    collisionNode = partControl.getNode();
  }
  
  @Override
  public void trigger() {
    
    if(canShoot) {
      canShoot = false;
      pe.emitAllParticles(); // for immediate shot
      
      // attach the collision cube to the root node, not the player node!
      Game.getInstance().getApp().enqueue(new AttachToNodeCallable(
              Game.getInstance().getRootNode(), collisionNode));
      
      
      Vector3f loc = mother.getWorldTranslation();
      if(vector.x > 0f) {
        loc.addLocal(1.2f, -0.3f, 0f);
      } else {
        loc.addLocal(-1.2f, -0.3f, 0f);
      }
      collisionNode.setLocalTranslation(loc);
      
      partControl.setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      
      if(partControl.getGeometryCollisionControl().collidedPlayer()) {
        canShoot = true;
        partControl.setEnabled(false);

        // remove the cube from the field
        Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(
                Game.getInstance().getRootNode(), collisionNode));
      } else {
        
      }
    }
  }
}
