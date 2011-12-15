/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.TeddyServerData;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageTransferServerData extends NetworkMessageManipulation {

  private TeddyServerData data;
  
  public ManMessageTransferServerData() {
    super();
  }
  
  public ManMessageTransferServerData(TeddyServerData data) {
    super();
    setData(data);
  }

  public TeddyServerData getData() {
    return data;
  }

  private void setData(TeddyServerData data) {
    this.data = data;
  }
}
