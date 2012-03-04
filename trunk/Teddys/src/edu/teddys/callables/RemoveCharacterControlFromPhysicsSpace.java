/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Node;
import edu.teddys.MegaLogger;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class RemoveCharacterControlFromPhysicsSpace implements Callable {

  private PhysicsSpace space;
  private CharacterControl node;
  
  public RemoveCharacterControlFromPhysicsSpace(PhysicsSpace space, CharacterControl node) {
    this.space = space;
    this.node = node;
  }
  
  public Object call() throws Exception {
    space.remove(node);
    MegaLogger.getLogger().debug(String.format("CharacterControl %s removed from PhysicsSpace %s", node, space));
    return null;
  }
  
}
