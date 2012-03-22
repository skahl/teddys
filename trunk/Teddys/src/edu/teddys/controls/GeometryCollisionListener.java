/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.objects.weapons.Weapon;
import edu.teddys.states.Game;
import org.apache.commons.math.random.RandomDataImpl;

/**
 *
 * @author skahl
 */
public class GeometryCollisionListener implements PhysicsCollisionListener {
  private boolean isListening;
  private boolean collided;
  private boolean collidedPlayer;
  private boolean doDamage;
  private Node node;
  private Weapon weapon;

  public GeometryCollisionListener(Node node, Weapon weapon) {
    this.node = node;
    isListening = false;
    collided = false;
    collidedPlayer = false;
    doDamage = true;
    
    this.weapon = weapon;
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
    String nodeA = event.getNodeA().getName();
    String nodeB = event.getNodeB().getName();
    
    if(nodeA.equals(node.getName())) {
      
      if(nodeB.contains("player")) {
        if(!collidedPlayer) {
          collidedPlayer = true;
          Integer hitPlayerID = Integer.parseInt(nodeB.substring(6));
          
          trigger(hitPlayerID);
        }
      }
      
      collided = true;
      
    } else if(nodeB.contains(node.getName())) {
      
      if(nodeA.contains("player")) {
        if(!collidedPlayer) {
          collidedPlayer = true;
          Integer hitPlayerID = Integer.parseInt(nodeA.substring(6));
          
          trigger(hitPlayerID);
        }
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
  
  private void trigger(Integer hitPlayerID) {
    if(weapon != null) {
        
        // damage calculation
        RandomDataImpl rnd = new RandomDataImpl();
        Float weaponDamage = FastMath.abs((float)rnd.nextGaussian(weapon.getBaseDamage(), GameSettings.DAMAGE_SIGMA));
        Float weaponAccuracy = FastMath.abs((float)rnd.nextGaussian(weapon.getAccuracy(), GameSettings.DAMAGE_SIGMA));
        Float damage = GameSettings.DAMAGE_MAX * weaponDamage * weaponAccuracy;

        int resDamage = (int) Math.ceil(damage);

        ManMessageSendDamage msg = new ManMessageSendDamage(weapon.getPlayerID(), 
                hitPlayerID, resDamage);
        
        TeddyServer.getInstance().send(msg);
      }
  }
}
