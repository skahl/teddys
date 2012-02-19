/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class DetachFromNodeCallable implements Callable {

  Node parent;
  Spatial child;

  public DetachFromNodeCallable(Node parent, Spatial child) {
    this.parent = parent;
    this.child = child;
  }

  public Object call() {
    parent.detachChild(child);
    return null;
  }
}
