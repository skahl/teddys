/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author cm
 */
@Serializable
public class NetworkMessageSpidermonkey extends AbstractMessage {

  private NetworkMessage data;

  public NetworkMessageSpidermonkey() {
    setReliable(NetworkSettings.useUDPIfAvailable);
  }

  public NetworkMessageSpidermonkey(NetworkMessage message) {
    this();
    this.data = message;
  }
  
  public NetworkMessage getData() {
    return data;
  }
}
