/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import edu.teddys.timer.ServerTimer;

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

  private Long localTimestamp = 0L;
  /**
   * This is a so-called "tick" which describes a world state.
   */
  private Long serverTimestamp = 0L;

  public Long getLocalTimestamp() {
    return localTimestamp;
  }

  /**
   * Should be set because of the latency calculation.
   * 
   * @param timestamp 
   */
  public void setLocalTimestamp(Long timestamp) {
    this.localTimestamp = timestamp;
  }

  public Long getServerTimestamp() {
    return serverTimestamp;
  }

  public void setServerTimestamp(Long serverTimestamp) {
    this.serverTimestamp = serverTimestamp;
  }

  public NetworkMessage() {
    // initialize the timestamp value
    if (ServerTimer.isActive()) {
      localTimestamp = ServerTimer.getServerTimestamp();
    }
  }
}
