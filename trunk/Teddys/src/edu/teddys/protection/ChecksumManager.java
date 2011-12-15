/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.protection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cm
 */
public class ChecksumManager {

  /**
   * The intervall for checksum checks
   */
  static Integer timerIntervall = new Integer(3000);
  private static ChecksumManagerThread thread = new ChecksumManagerThread();
  static Map<String, String> result = new HashMap<String, String>();
  static Map<String, List<String>> files = new HashMap<String, List<String>>();

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
    System.out.println("Checksum timer thread spawned. Sending a request every " + timerIntervall / 1000 + " seconds.");
  }

  public static void stopTimer() {
    try {
      thread.join();
      System.out.println("The checksum timer has been stopped.");
    } catch (InterruptedException ex) {
      Logger.getLogger(ChecksumManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void checkChecksum(String token, String input) throws VerifyError {
    // check if the key has been invalidated
    if(!result.containsKey(token)) {
      throw new VerifyError("Checksum validation failed. Token is invalid!");
    }
    if (!input.equals(result.get(token))) {
      throw new VerifyError("Checksum validation failed. Invalid checksum!");
    }
  }
}
