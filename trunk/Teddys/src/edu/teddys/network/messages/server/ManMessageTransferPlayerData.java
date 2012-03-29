/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.ClientData;
import edu.teddys.network.messages.NetworkMessageManipulation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageTransferPlayerData extends NetworkMessageManipulation {

  private List<ClientData> data = new ArrayList<ClientData>();

  public ManMessageTransferPlayerData() {
    super();
  }

  public ManMessageTransferPlayerData(List<ClientData> data) {
    this();
    setData(data);
  }

  public List<ClientData> getData() {
    return data;
  }

  private void setData(List<ClientData> data) {
    this.data = data;
  }
}
