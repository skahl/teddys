/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;
import java.awt.Point;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageSendPosition extends NetworkMessageManipulation {
  
  private Point position;
  
  public ManMessageSendPosition(Point position) {
    //TODO ignore too large distances?
    if(position.x < 0 || position.y < 0) {
      throw new InstantiationError("Position must be defined by positive values!");
    }
    setPosition(position);
  }

  public Point getPosition() {
    return position;
  }

  private void setPosition(Point position) {
    this.position = position;
  }
}
