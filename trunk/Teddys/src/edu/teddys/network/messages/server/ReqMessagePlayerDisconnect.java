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
public class ReqMessagePlayerDisconnect extends NetworkMessageRequest {
  
  private Integer clientID = new Integer(0);
  
  public ReqMessagePlayerDisconnect() {
    super();
  }
  
  public ReqMessagePlayerDisconnect(Integer clientID) {
    this();
    this.clientID = clientID;
  }

  public Integer getClientID() {
    return clientID;
  }

  public void setClientID(Integer clientID) {
    this.clientID = clientID;
  }
}
