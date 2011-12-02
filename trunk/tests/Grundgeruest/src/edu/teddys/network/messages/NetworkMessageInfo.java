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
  
  private String message;
  private Integer[] recipients;
  
  public NetworkMessageInfo() {
//    throw new InstantiationError("Source, message and optionally recipients are not defined!");
  }
  
  public NetworkMessageInfo(String message, Integer[] recipients) {
    setMessage(message);
    setRecipients(recipients);
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
