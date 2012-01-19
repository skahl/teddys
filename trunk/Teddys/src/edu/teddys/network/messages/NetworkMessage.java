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
 * the source (sender). For clients, the ID is positive (>= 0). The server
 * however uses ID 0 as well.
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

  /**
   * Must be set because of the latency calculation.
   * 
   * @param timestamp 
   */
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }
  
  public NetworkMessage() {
    // initialize the timestamp value
    setTimestamp(getSystemTimestamp());
  }
  
  public static Long getSystemTimestamp() {
    return System.currentTimeMillis();
  }
}
