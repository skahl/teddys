/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageRequest;

/**
 *
 * @author cm
 */
@Serializable
public class ReqMessagePauseRequest extends NetworkMessageRequest {
  
  private boolean paused;
  
  public ReqMessagePauseRequest() {
    super();
  }
  
  public ReqMessagePauseRequest(boolean paused) {
    setPaused(paused);
  }

  public boolean isPaused() {
    return paused;
  }

  private void setPaused(boolean paused) {
    this.paused = paused;
  }
}
