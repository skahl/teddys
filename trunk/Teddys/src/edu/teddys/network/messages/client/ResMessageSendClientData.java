/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.ClientData;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.NetworkMessageResponse;

/**
 *
 * @author cm
 */
@Serializable
public class ResMessageSendClientData extends NetworkMessageResponse {
  
  private ClientData data;

  public ResMessageSendClientData() {
    super();
  }

  public ResMessageSendClientData(ClientData data) {
    setClientData(data);
  }

  public ClientData getClientData() {
    return data;
  }

  private void setClientData(ClientData data) {
    this.data = data;
  }
}
