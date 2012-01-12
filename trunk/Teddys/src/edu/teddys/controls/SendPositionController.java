/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import edu.teddys.BaseGame;
import java.util.logging.Level;

/**
 *
 * @author cm
 */
public class SendPositionController {

  static Integer timerIntervall = new Integer(30);
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
    BaseGame.getLogger().log(
            Level.INFO,
            "SendPosition timer thread spawned. Sending a request every {0} ms.",
            timerIntervall);
  }

  public static void stopTimer() {
    try {
      thread.join();
    } catch (InterruptedException ex) {
      BaseGame.getLogger().log(
              Level.INFO,
              "The SendPosition timer could not be stopped:{0}",
              ex.getMessage());
    }
  }
}
