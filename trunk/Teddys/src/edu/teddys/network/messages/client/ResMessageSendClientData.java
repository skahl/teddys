/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.NetworkMessageResponse;

/**
 *
 * @author cm
 */
@Serializable
public class ResMessageSendClientData extends NetworkMessageResponse {
  //TODO create a TeddyClientData class and transfer it!

  private TeddyClient data;

  public ResMessageSendClientData() {
    super();
  }

  public ResMessageSendClientData(TeddyClient data) {
    setClientData(data);
  }

  public TeddyClient getClientData() {
    return data;
  }

  private void setClientData(TeddyClient data) {
    this.data = data;
  }
}
