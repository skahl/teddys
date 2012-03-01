/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;

/**
 *
 * The ServerTimer does the whole logic of a game with registered players.
 * For example,
 * - process user input
 * - trigger events
 * - check game rules
 * - update all object states
 * (According to https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking )
 * 
 * @author cm
 */
public class ServerTimer {
  
  private static ServerTimerThread thread;
  private static Long tickBuffer = null;
  
  /**
   * Start the server timer.
   */
  public static void startTimer() {
    if (thread != null) {
      return;
    }
    thread = new ServerTimerThread();
    if(tickBuffer != null) {
      thread.setTick(tickBuffer);
      tickBuffer = null;
    }
    thread.start();
    String tempMsg = String.format(
            "Server timer thread spawned (Rate: %f, Interval: %d ms)",
            (Float) (1f / GameSettings.SERVER_TIMESTAMP_INTERVAL * 1000),
            GameSettings.SERVER_TIMESTAMP_INTERVAL
            );
    MegaLogger.getLogger().debug(tempMsg);
  }

  public static void stopTimer() {
    if (thread == null || !thread.isAlive()) {
      return;
    }
    thread.stopThread();
    thread = null;
    MegaLogger.getLogger().debug("Server timer stopped.");
  }
  
  public static boolean isActive() {
    if(thread == null) {
      return false;
    }
    return thread.isAlive();
  }
  
  public static Long getServerTimestamp() {
    return thread.getTick();
  }
  
  public synchronized static void setServerTimestamp(Long ts) {
    if(thread == null) {
      tickBuffer = ts;
      return;
    }
    thread.setTick(ts);
  }
}
