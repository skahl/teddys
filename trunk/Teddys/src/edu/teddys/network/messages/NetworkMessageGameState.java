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
public class NetworkMessageGameState extends NetworkMessage {

  public NetworkMessageGameState() {
    super();
  }
  
  public NetworkMessageGameState(Integer[] recipients) {
    super(recipients);
  }
}
