/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameMode;
import edu.teddys.MegaLogger;

/**
 *
 * @author cm
 */
public class MatchTimerThread extends Thread {

  private boolean stop = false;
  private Integer seconds = 0;
  private Integer maxSeconds = 600;
  private GameMode gameModeInstance = null;
  
  /**
   * 
   * Initialize this thead with some values of the GameMode.
   * 
   * @param mode The game mode, such as Deathmatch
   */
  public MatchTimerThread(GameMode mode) {
    this.gameModeInstance = mode;
    maxSeconds = mode.getMaxMinutes()*60;
  }

  @Override
  public void run() {
    
    while (!stop) {
      if(seconds >= maxSeconds) {
        MegaLogger.getLogger().info("Current game session has expired " + maxSeconds + " seconds playtime.");
        if(gameModeInstance != null) {
          gameModeInstance.stop();
        }
      }
      seconds++;
      try {
        sleep(1000);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }

  /**
   * 
   * Set the flag to stop the current thread.
   * 
   */
  void stopThread() {
    stop = true;
  }
}
