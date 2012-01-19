/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.BaseGame;
import edu.teddys.MegaLogger;
import java.util.logging.Level;

/**
 * 
 * Triggers a network message to sync the current server data.
 *
 * @author cm
 */
public class ServerDataSync {

  //TODO put into the settings class
  /**
   * Intervall in ms
   * 
   */
  static Integer timerIntervall = new Integer(2000);
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
    MegaLogger.debug("ServerDataSync timer thread spawned. "
            + "Sending a request every "+timerIntervall+" ms.");
  }

  public static void stopTimer() {
    if(!thread.isAlive()) {
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
