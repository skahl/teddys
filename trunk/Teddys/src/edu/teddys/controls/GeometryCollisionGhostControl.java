/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import edu.teddys.MegaLogger;
import edu.teddys.states.Game;

/**
 *
 * @author skahl
 */
public class GeometryCollisionGhostControl extends GhostControl implements PhysicsCollisionListener {
  private boolean isListening;
  private boolean collided;
  private boolean collidedPlayer;

  public GeometryCollisionGhostControl(CollisionShape cs) {
    isListening = false;
    collided = false;
    collidedPlayer = false;
    setCollisionShape(cs);
  }
  
  public void setListen(boolean listen) {
    
    if(isListening && !listen) {
      isListening = false;
      Game.getInstance().getBulletAppState().getPhysicsSpace().remove(spatial);
      Game.getInstance().getBulletAppState().getPhysicsSpace().removeCollisionListener(this);
      
      reset();
    } else if(!isListening && listen) {
      isListening = true;
      Game.getInstance().getBulletAppState().getPhysicsSpace().add(spatial);
      Game.getInstance().getBulletAppState().getPhysicsSpace().addCollisionListener(this);
    }
  }
  
  public void collision(PhysicsCollisionEvent event) {
    
    if(event.getNodeA().getName().equals(spatial.getName())) {
      
      if(event.getNodeB().getName().contains("player")) {
        collidedPlayer = true;
        
        MegaLogger.getLogger().info(event.getNodeB().getName()+" was hit by "+spatial.getName());
      }
      
      collided = true;
    } else if(event.getNodeB().getName().contains(spatial.getName())) {
      
      if(event.getNodeA().getName().contains("player")) {
        collidedPlayer = true;
        
        MegaLogger.getLogger().info(event.getNodeA().getName()+" was hit by "+spatial.getName());
      }
      
      collided = true;
    }
  }
  
  public boolean collided() {
    return collided;
  }
  
  public boolean collidedPlayer() {
    return collidedPlayer;
  }
  
  private void reset() {
    collided = false;
    collidedPlayer = false;
  }
}
