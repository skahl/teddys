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
public class AddPhysicsCollisionListener implements Callable {

  private PhysicsSpace space;
  private PhysicsCollisionListener listener;

  public AddPhysicsCollisionListener(PhysicsSpace space, PhysicsCollisionListener listener) {
    this.space = space;
    this.listener = listener;
  }

  public Object call() throws Exception {
    space.addCollisionListener(listener);
    MegaLogger.getLogger().debug("Added PhysicsCollisionListener " + listener + " to the PhysicsSpace " + space);
    return null;
  }
}
