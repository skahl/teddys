/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;

/**
 *
 * The ClientTimer sends a dump of the current keyboard and mouse state
 * in the same interval as the server parses the events 
 * (see GameSettings.SERVER_TIMESTAMP_INTERVAL).
 * 
 * (According to https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking )
 * 
 * @author cm
 */
public class ClientTimer {
  
  private static ServerTimerThread thread = new ServerTimerThread();
  /**
   * The last known server timestamp extracted from a server message
   */
  public static volatile Long serverTimestamp = 0L;
  /**
   * Start the server timer.
   */
  public static void startTimer() {
    if (thread.isAlive()) {
      return;
    }
    thread.start();
    String tempMsg = String.format(
            "Client timer thread spawned (Rate: %f, Interval: %d ms)",
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
      MegaLogger.debug("Client timer thread joined.");
    } catch (InterruptedException ex) {
      MegaLogger.debug(new Throwable("Error while trying to join the thread!", ex));
    }
  }
}
