/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import edu.teddys.MegaLogger;

/**
 *
 * @author cm
 */
public class SendPositionController {

  //TODO Put into the settings class
  static Integer timerIntervall = new Integer(50);
  private static SendPositionControllerThread thread = new SendPositionControllerThread();

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
    MegaLogger.debug("SendPosition timer thread spawned. "
            + "Sending a request every " + timerIntervall + " ms.");
  }

  public static void stopTimer() {
    if (!thread.isAlive()) {
      return;
    }
    thread.interrupt();
    try {
      thread.join();
      MegaLogger.debug("SendPosition timer thread joined.");
    } catch (InterruptedException ex) {
      MegaLogger.debug(new Throwable("Error while trying to join the thread!", ex));
    }
  }
}
