/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * The base class of network messages. There is always one mandatory field:
 * the source (sender). For clients, this is an ID greater than 1. The server
 * however uses ID 0.
 * 
 * To prevent multiple transfers of the same message maybe caused by a defect
 * network infrastructure, use a timestamp for identification which is auto-
 * generated.
 *
 * @author cm
 */
@Serializable
public class NetworkMessage extends AbstractMessage {

  private Long timestamp;

  public Long getTimestamp() {
    return timestamp;
  }

  private void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }
  
  public NetworkMessage() {
    // update the timestamp value
    setTimestamp(System.currentTimeMillis() / 1000);
  }
}
