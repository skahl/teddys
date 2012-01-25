/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageGameState;

/**
 *
 * @author cm
 */
@Serializable
public class GSMessageBeginGame extends NetworkMessageGameState {
  
  public GSMessageBeginGame() {
    super();
  }
}
