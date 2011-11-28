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

  private NetworkData data;

  public NetworkMessageSpidermonkey() {
    setReliable(NetworkSettings.useUDPIfAvailable);
  }

  public NetworkMessageSpidermonkey(NetworkData message) {
    this();
    this.data = message;
  }
  
  public NetworkData getData() {
    return data;
  }
}
