/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Node;
import edu.teddys.MegaLogger;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class AddNodeToPhysicsSpaceCallable implements Callable {

  private PhysicsSpace space;
  private Node node;
  
  public AddNodeToPhysicsSpaceCallable(PhysicsSpace space, Node node) {
    this.space = space;
    this.node = node;
  }
  
  public Object call() throws Exception {
    space.add(node);
    MegaLogger.getLogger().debug(String.format("Node %s attached to PhysicsSpace %s", node, space));
    return null;
  }
  
}
