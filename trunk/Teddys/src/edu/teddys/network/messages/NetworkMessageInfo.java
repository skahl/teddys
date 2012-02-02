/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author cm
 */
@Serializable
public class NetworkMessageInfo extends NetworkMessage {
  
  private String message = "";
  
  public NetworkMessageInfo() {
    super();
//    throw new InstantiationError("Source, message and optionally recipients are not defined!");
  }
  
  public NetworkMessageInfo(Integer[] recipients, String message) {
    super(recipients);
    if(message == null || message.isEmpty()) {
      throw new InstantiationError("Message can't be null or empty!");
    }
    setMessage(message);
  }
  
  public NetworkMessageInfo(Integer recipient, String message) {
    this(new Integer[]{recipient}, message);
  }
  
  public NetworkMessageInfo(String message) {
    this(new Integer[]{}, message);
  }

  public String getMessage() {
    return message;
  }

  final protected void setMessage(String message) {
    this.message = message;
  }
}
