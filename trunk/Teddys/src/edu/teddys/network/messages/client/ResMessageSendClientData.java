/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.ClientData;
import edu.teddys.network.messages.NetworkMessageResponse;

/**
 *
 * @author cm
 */
@Serializable
public class ResMessageSendClientData extends NetworkMessageResponse {
  
  private ClientData data = new ClientData();

  public ResMessageSendClientData() {
    super();
  }

  public ResMessageSendClientData(ClientData data) {
    this();
    if(data == null) {
      throw new InstantiationError("ClientData can't be null!");
    }
    setClientData(data);
  }

  public ClientData getClientData() {
    return data;
  }

  private void setClientData(ClientData data) {
    this.data = data;
  }
}
