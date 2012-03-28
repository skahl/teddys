/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import edu.teddys.MegaLogger;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class RemovePhysicsCollisionListener implements Callable {

  private PhysicsSpace space;
  private PhysicsCollisionListener listener;

  public RemovePhysicsCollisionListener(PhysicsSpace space, PhysicsCollisionListener listener) {
    this.space = space;
    this.listener = listener;
  }

  public Object call() throws Exception {
    space.removeCollisionListener(listener);
    MegaLogger.getLogger().debug("Removed PhysicsCollisionListener " + listener + " from the PhysicsSpace " + space);
    return null;
  }
}
