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
public class GSMessageGamePaused extends NetworkMessageGameState {

  private boolean paused = true;
  
  public GSMessageGamePaused() {
    super();
  }

  public GSMessageGamePaused(boolean paused) {
    this();
    setPaused(paused);
  }

  public boolean isPaused() {
    return paused;
  }

  private void setPaused(boolean paused) {
    this.paused = paused;
  }
}
