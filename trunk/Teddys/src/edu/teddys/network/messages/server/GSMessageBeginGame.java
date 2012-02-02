/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageGameState;

/**
 *
 * A message for the clients to load the game.
 * 
 * @author cm
 */
@Serializable
public class GSMessageBeginGame extends NetworkMessageGameState {
  
  public GSMessageBeginGame() {
    super();
  }
  
  public GSMessageBeginGame(Integer clientID) {
    this(new Integer[]{clientID});
  }
  
  public GSMessageBeginGame(Integer[] clientIDs) {
    super(clientIDs);
  }
}
