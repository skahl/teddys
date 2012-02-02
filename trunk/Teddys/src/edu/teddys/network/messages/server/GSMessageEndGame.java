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
public class GSMessageEndGame extends NetworkMessageGameState {
  
  public GSMessageEndGame() {
    super();
  }
  
  public GSMessageEndGame(Integer clientID) {
    super(new Integer[]{clientID});
  }
}
