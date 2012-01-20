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
  
  private static ServerTimerThread thread = new ServerTimerThread();
  /**
   * Start the server timer.
   */
  public static void startTimer() {
    if (thread.isAlive()) {
      return;
    }
    thread.start();
    String tempMsg = String.format(
            "Server timer thread spawned (Rate: %f, Interval: %d ms)",
            (Float) (1f / GameSettings.SERVER_TIMESTAMP_INTERVAL),
            GameSettings.SERVER_TIMESTAMP_INTERVAL
            );
    MegaLogger.debug(tempMsg);
  }

  public static void stopTimer() {
    if (!thread.isAlive()) {
      return;
    }
    thread.interrupt();
    try {
      thread.join();
      MegaLogger.debug("Server timer thread joined.");
    } catch (InterruptedException ex) {
      MegaLogger.debug(new Throwable("Error while trying to join the thread!", ex));
    }
  }
  
  public static Long getServerTimestamp() {
    return thread.getTick();
  }
}
