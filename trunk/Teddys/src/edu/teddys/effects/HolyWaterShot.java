
package edu.teddys.effects;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.controls.GeometryCollisionGhostControl;
import edu.teddys.states.Game;


/**
 * Implements the HolyWaterShot Effect. Triggering a splash of holy water. Water disappears on impact. 
 * 
 * @author skahl
 */
public class HolyWaterShot extends GunShot {
  Box box;
  Geometry collideBox;
  GeometryCollisionGhostControl colControl;
  
  
  public HolyWaterShot() {
    this.init("Holy Water", Game.getInstance().getAssetManager(), "Textures/Effects/plush/plush.png");
    
    // set particle emitter stuff
    
    
    
    // set collision stuff
    
    box = new Box(0.4f, 0.4f, 0.5f);
    collideBox = new Geometry(getNode().getName(), box);
    Material red = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    red.setColor("Color", ColorRGBA.Red);
    collideBox.setMaterial(red);
    collideBox.setCullHint(Spatial.CullHint.Inherit); // TODO: make invisible
    
    colControl = new GeometryCollisionGhostControl(new BoxCollisionShape(
            new Vector3f(box.xExtent, box.yExtent, box.zExtent)));
    
    collideBox.addControl(colControl);
    
  }
  
  @Override
  public void trigger() {
    
    if(canShoot) {
      canShoot = false;
      pe.emitAllParticles(); // for immediate shot
      
      Game.getInstance().getApp().enqueue(new AttachToNodeCallable(mother, collideBox));
      colControl.setListen(true);
      
      if(vector.x > 0f) {
        collideBox.setLocalTranslation(1f, 0f, 0f);
      } else {
        collideBox.setLocalTranslation(-1f, 0f, 0f);
      }
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      canShoot = true;
      colControl.setListen(false);
      Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(mother, collideBox));
    }
  }
}
