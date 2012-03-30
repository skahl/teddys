/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.SessionDataListener;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.GSMessageEndGame;
import edu.teddys.timer.MatchTimer;

/**
 *
 * @author cm
 */
@Serializable
abstract public class GameMode implements SessionDataListener {
  
  protected Integer minutesToPlay = 5;
  
  protected GameModeEnum modeEnum;
  
  public GameModeEnum getEnum() {
    return modeEnum;
  }
  
  public Integer getMaxMinutes() {
    return minutesToPlay;
  }
  
  public String getName() {
    return modeEnum.name();
  }
  
  /**
   * Called when a new Game has begun.
   * 
   * Starts the MatchTimer. Note: This function calls MatchTimer.stopTimer() at first.
   */
  public void start() {
    MatchTimer.stopTimer();
    MatchTimer.startTimer(this);
  }
  
  /**
   * Called when the MatchTimer thread stopped the activity.
   * 
   * Frees the resources and send a network message.
   */
  public void stop() {
    // Deallocate the resources
    MatchTimer.stopTimer();
    GSMessageEndGame endGame = new GSMessageEndGame();
    TeddyServer.getInstance().send(endGame);
    //TODO start a new game?
  }
  
}
