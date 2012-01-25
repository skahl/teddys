/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
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
  private Vector3f position = new Vector3f();
  /**
   * The current animation to be displayed on the other clients.
   */
  private String currentAnimSequence = "";
  
  public ManMessageSendPosition() {
    super();
  }
  
  public ManMessageSendPosition(Vector3f position) {
    this();
    //TODO ignore too large distances (according to the map)?
    setPosition(position);
  }

  public Vector3f getPosition() {
    return position;
  }

  private void setPosition(Vector3f position) {
    this.position = position;
  }

  public String getCurrentAnimSequence() {
    return currentAnimSequence;
  }

  public void setCurrentAnimSequence(String currentAnimSequence) {
    this.currentAnimSequence = currentAnimSequence;
  }
}
