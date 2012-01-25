/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageGameState;

/**
 *
 * @author cm
 */
@Serializable
public class GSMessagePlayerReady extends NetworkMessageGameState {

  public GSMessagePlayerReady() {
    super();
  }
}
