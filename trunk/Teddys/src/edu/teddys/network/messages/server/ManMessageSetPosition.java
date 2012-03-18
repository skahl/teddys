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

  private Integer clientID = new Integer(0);
  private Vector3f position = new Vector3f();

  public ManMessageSetPosition() {
    super();
  }

  public ManMessageSetPosition(Integer clientID, Vector3f pos) {
    this();
    this.clientID = clientID;
    this.position = pos;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f newPosition) {
    this.position = newPosition;
  }

  public Integer getClientID() {
    return clientID;
  }

  public void setClientID(Integer clientID) {
    this.clientID = clientID;
  }
}
