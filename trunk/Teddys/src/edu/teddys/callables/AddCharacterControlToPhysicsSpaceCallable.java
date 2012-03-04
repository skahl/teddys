/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.CharacterControl;
import edu.teddys.MegaLogger;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class AddCharacterControlToPhysicsSpaceCallable implements Callable {

  private PhysicsSpace space;
  private CharacterControl control;
  
  public AddCharacterControlToPhysicsSpaceCallable(PhysicsSpace space, CharacterControl control) {
    this.space = space;
    this.control = control;
  }
  
  public Object call() throws Exception {
    space.add(control);
    MegaLogger.getLogger().debug(String.format("CharacterControl %s attached to PhysicsSpace %s", control, space));
    return null;
  }
  
}
