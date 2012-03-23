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
public class ManCursorPosition extends NetworkMessageManipulation {
  
  private Vector3f cursor = new Vector3f();
  
  public ManCursorPosition() {
    super();
  }
  
  public ManCursorPosition(Vector3f cursor) {
    this.cursor = cursor;
  }

  public Vector3f getCursor() {
    return cursor;
  }

  public void setCursor(Vector3f cursor) {
    this.cursor = cursor;
  }
  
  
}
