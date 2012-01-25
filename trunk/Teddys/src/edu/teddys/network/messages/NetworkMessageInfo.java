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
  private Integer[] recipients;
  
  public NetworkMessageInfo() {
    super();
//    throw new InstantiationError("Source, message and optionally recipients are not defined!");
  }
  
  public NetworkMessageInfo(String message, Integer[] recipients) {
    super();
    if(message == null || message.isEmpty()) {
      throw new InstantiationError("Message can't be null or empty!");
    }
    setMessage(message);
    setRecipients(recipients);
  }
  
  public NetworkMessageInfo(String message, Integer recipient) {
    this(message, new Integer[]{recipient});
  }
  
  public NetworkMessageInfo(String message) {
    this(message, new Integer[]{});
  }

  public String getMessage() {
    return message;
  }

  final protected void setMessage(String message) {
    this.message = message;
  }

  public Integer[] getRecipients() {
    return recipients;
  }

  final protected void setRecipients(Integer[] recipients) {
    this.recipients = recipients;
  }
  
  
}
