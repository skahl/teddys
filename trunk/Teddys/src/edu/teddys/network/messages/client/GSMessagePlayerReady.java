/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageGameState;

/**
 *
 * The message that the specified player is ready to begin the game.
 * 
 * @author cm
 */
@Serializable
public class GSMessagePlayerReady extends NetworkMessageGameState {
  
  private Integer source = new Integer(0);

  public GSMessagePlayerReady() {
    super();
  }
  
  public GSMessagePlayerReady(Integer source) {
    this.source = source;
  }

  public Integer getSource() {
    return source;
  }
}
