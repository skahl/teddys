/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;

/**
 * 
 * Triggers a network message to sync the current server data.
 *
 * @author cm
 */
public class ServerDataSync {

  private static ServerDataSyncThread thread = new ServerDataSyncThread();

  /**
   * Start the checksum manager timer. This sends checksum requests every intervall
   * to the clients of the server.
   */
  public static void startTimer() {
    if (thread.isAlive()) {
      return;
    }
    thread.start();
    String tempMsg = String.format(
            "Checksum timer thread spawned (Rate: %f, Interval: %d ms)",
            (Float) (1f / GameSettings.SERVER_SYNC_INTERVAL),
            GameSettings.SERVER_SYNC_INTERVAL
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
      MegaLogger.debug("ServerDataSync timer thread joined.");
    } catch (InterruptedException ex) {
      MegaLogger.debug(new Throwable("Error while trying to join the thread!", ex));
    }
  }
}
