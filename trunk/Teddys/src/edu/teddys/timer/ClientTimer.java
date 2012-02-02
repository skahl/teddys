/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.input.InputTuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
  public static List<InputTuple> input = Collections.synchronizedList(new ArrayList<InputTuple>());

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
            "Client timer thread spawned (Rate: %d, Interval: %f ms)",
            GameSettings.NETWORK_CLIENT_TIMER_RATE,
            (Float) (1f / GameSettings.NETWORK_CLIENT_TIMER_RATE * 1000));
    MegaLogger.getLogger().debug(tempMsg);
  }

  public static void stopTimer() {
    if (thread == null || !thread.isAlive()) {
      return;
    }
    thread.stopThread();
    thread = null;
    input.clear();
    MegaLogger.getLogger().debug("Client timer thread joined.");
  }
  
  public static List<InputTuple> getInput() {
    return input;
  }
}
