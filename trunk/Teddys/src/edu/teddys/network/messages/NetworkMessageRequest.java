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
public class NetworkMessageRequest extends NetworkMessage {

  public NetworkMessageRequest() {
    super();
  }
  
  public NetworkMessageRequest(Integer[] recipients) {
    super(recipients);
  }
}
