/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import edu.teddys.timer.ServerTimer;
import java.util.Arrays;

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
  /**
   * The clients that should get this message.
   */
  private Integer[] recipients = new Integer[0];

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

  public Integer[] getRecipients() {
    return recipients;
  }

  public void setRecipients(Integer[] recipients) {
    this.recipients = recipients;
  }

  public NetworkMessage() {
    // initialize the timestamp value
    if (ServerTimer.isActive()) {
      localTimestamp = ServerTimer.getServerTimestamp();
    }
  }

  public NetworkMessage(Integer[] recipients) {
    this();
    this.recipients = recipients;
  }
  
  @Override
  public String toString() {
    return getClass().getName()+String.format("[LocalTS=%d,ServerTS=%d,Recipients=%s]",
            getLocalTimestamp(),
            getServerTimestamp(),
            Arrays.asList(getRecipients()));
  }
}
