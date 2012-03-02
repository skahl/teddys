/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author cm
 */
@Serializable
public class NetworkMessageResponse extends NetworkMessage {

  public NetworkMessageResponse() {
    super();
  }
  
  public NetworkMessageResponse(Integer[] recipients) {
    super(recipients);
  }
}
