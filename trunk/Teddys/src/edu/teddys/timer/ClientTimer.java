/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import java.util.LinkedList;
import java.util.Map.Entry;

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

  private static ClientTimerThread thread;
  /**
   * The last known server timestamp extracted from a server message
   */
  public static Long lastServerTimestamp = 0L;
  public static LinkedList<Entry<String, Object>> input = new LinkedList<Entry<String, Object>>();

  /**
   * Start the server timer.
   */
  public static void startTimer() {
    if (thread != null) {
      return;
    }
    thread = new ClientTimerThread();
    thread.start();
    String tempMsg = String.format(
            "Client timer thread spawned (Rate: %f, Interval: %d ms)",
            (Float) (1f / GameSettings.SERVER_TIMESTAMP_INTERVAL * 1000),
            GameSettings.SERVER_TIMESTAMP_INTERVAL);
    MegaLogger.getLogger().debug(tempMsg);
  }

  public static void stopTimer() {
    if (thread == null || !thread.isAlive()) {
      return;
    }
    thread.interrupt();
    thread = null;
    MegaLogger.getLogger().debug("Client timer thread joined.");
  }
}
