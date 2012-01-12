/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.input.Position;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageSetPosition extends NetworkMessageManipulation {

  private Position newPosition;

  public ManMessageSetPosition() {
    super();
  }

  public ManMessageSetPosition(Position pos) {
    super();
    setNewPosition(pos);
  }

  public Position getNewPosition() {
    return newPosition;
  }

  public void setNewPosition(Position newPosition) {
    this.newPosition = newPosition;
  }
}
