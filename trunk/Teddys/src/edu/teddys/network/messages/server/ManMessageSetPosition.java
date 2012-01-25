/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public final class ManMessageSetPosition extends NetworkMessageManipulation {

  private Vector3f newPosition;

  public ManMessageSetPosition() {
    super();
  }

  public ManMessageSetPosition(Vector3f pos) {
    this();
    setNewPosition(pos);
  }

  public Vector3f getNewPosition() {
    return newPosition;
  }

  public void setNewPosition(Vector3f newPosition) {
    this.newPosition = newPosition;
  }
}
