/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import edu.teddys.MegaLogger;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class AttachToNodeCallable implements Callable {

  Node parent;
  Spatial child;

  public AttachToNodeCallable(Node parent, Spatial child) {
    if(parent == null || child == null) {
      throw new ExceptionInInitializerError("Parent or child is null!");
    }
    this.parent = parent;
    this.child = child;
  }

  public Object call() {
    parent.attachChild(child);
    MegaLogger.getLogger().debug(String.format("Node %s attached to %s.", child, parent));
    return null;
  }
}
