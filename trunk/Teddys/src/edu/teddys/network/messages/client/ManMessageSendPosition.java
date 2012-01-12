/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.input.Position;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageSendPosition extends NetworkMessageManipulation {
  
  /**
   * The position on which the player is currently located.
   */
  private Position position;
  /**
   * The current animation to be displayed on the other clients.
   */
  private String currentAnimSequence;
  
  public ManMessageSendPosition() {
    super();
  }
  
  public ManMessageSendPosition(Position position) {
    super();
    //TODO ignore too large distances (according to the map)?
    setPosition(position);
  }

  public Position getPosition() {
    return position;
  }

  private void setPosition(Position position) {
    this.position = position;
  }

  public String getCurrentAnimSequence() {
    return currentAnimSequence;
  }

  public void setCurrentAnimSequence(String currentAnimSequence) {
    this.currentAnimSequence = currentAnimSequence;
  }
}
