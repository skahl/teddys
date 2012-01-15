/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.BaseGame;
import java.util.logging.Level;

/**
 * 
 * Triggers a network message to sync the current server data.
 *
 * @author cm
 */
public class ServerDataSync {

  /**
   * Intervall in ms
   */
  static Integer timerIntervall = new Integer(2000);
  private static ServerDataSyncThread thread = new ServerDataSyncThread();

  /**
   * Start the checksum manager timer. This sends checksum requests every intervall
   * to the clients of the server. Note: If all clients responded, use ready()
   * to generate a new list of files to be checked.
   */
  public static void startTimer() {
    if (thread.isAlive()) {
      return;
    }
    thread.start();
    BaseGame.getLogger().log(
            Level.INFO,
            "ServerDataSync timer thread spawned. Sending a request every {0} ms.",
            timerIntervall);
  }

  public static void stopTimer() {
    if(!thread.isAlive()) {
      return;
    }
    thread.interrupt();
    try {
      thread.join();
    } catch (InterruptedException ex) {
      BaseGame.getLogger().log(
              Level.INFO,
              "The ServerDataSync timer could not be stopped:{0}",
              ex.getMessage());
    }
  }
}
