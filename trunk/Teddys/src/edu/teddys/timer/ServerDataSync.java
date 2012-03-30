/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;

/**
 * 
 * Triggers a network message to sync the current server data, i.e. Player data.
 *
 * @author cm
 */
public class ServerDataSync {

  private static ServerDataSyncThread thread;

  /**
   * Note: If an instance is already active, just return.
   */
  public static void startTimer() {
    if (thread != null) {
      return;
    }
    thread = new ServerDataSyncThread();
    thread.start();
    String tempMsg = String.format(
            "ServerDataSync timer thread spawned (Rate: %f, Interval: %d ms)",
            (Float) (1f / GameSettings.NETWORK_SERVER_SYNC_INTERVAL * 1000),
            GameSettings.NETWORK_SERVER_SYNC_INTERVAL);
    MegaLogger.getLogger().debug(tempMsg);
  }

  public static void stopTimer() {
    if (!thread.isAlive()) {
      return;
    }
    thread.stopThread();
    thread = null;
    MegaLogger.getLogger().debug("ServerDataSync timer thread joined.");
  }
}
