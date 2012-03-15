/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Node;
import edu.teddys.MegaLogger;
import edu.teddys.states.Game;

/**
 *
 * @author skahl
 */
public class GeometryCollisionListener implements PhysicsCollisionListener {
  private boolean isListening;
  private boolean collided;
  private boolean collidedPlayer;
  private Node node;

  public GeometryCollisionListener(Node node) {
    this.node = node;
    isListening = false;
    collided = false;
    collidedPlayer = false;
  }
  
  public void setListen(boolean listen) {
    
    if(isListening && !listen) {
      isListening = false;
      Game.getInstance().getBulletAppState().getPhysicsSpace().remove(node);
      Game.getInstance().getBulletAppState().getPhysicsSpace().removeCollisionListener(this);
      
      reset();
    } else if(!isListening && listen) {
      isListening = true;
      Game.getInstance().getBulletAppState().getPhysicsSpace().add(node);
      Game.getInstance().getBulletAppState().getPhysicsSpace().addCollisionListener(this);
    }
  }
  
  public void collision(PhysicsCollisionEvent event) {
    
    if(event.getNodeA().getName().equals(node.getName())) {
      
      if(event.getNodeB().getName().contains("player")) {
        collidedPlayer = true;
        
        MegaLogger.getLogger().info(event.getNodeB().getName()+" was hit by "+node.getName());
      }
      
      collided = true;
      
    } else if(event.getNodeB().getName().contains(node.getName())) {
      
      if(event.getNodeA().getName().contains("player")) {
        collidedPlayer = true;
        
        MegaLogger.getLogger().info(event.getNodeA().getName()+" was hit by "+node.getName());
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
