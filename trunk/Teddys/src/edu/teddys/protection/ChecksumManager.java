/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.protection;

import edu.teddys.BaseGame;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

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
    BaseGame.getLogger().log(
            Level.INFO,
            "Checksum timer thread spawned. Sending a request every {0} seconds.",
            timerIntervall / 1000);
  }

  public static void stopTimer() {
    if(!thread.isAlive()) {
      return;
    }
    try {
      thread.join();
      BaseGame.getLogger().info("The checksum timer has been stopped.");
    } catch (InterruptedException ex) {
      BaseGame.getLogger().log(
              Level.INFO,
              "The checksum timer could not be stopped:{0}",
              ex.getMessage());
    }
  }

  public static void checkChecksum(String token, String input) throws VerifyError {
    // check if the key has been invalidated
    if (!result.containsKey(token)) {
      throw new VerifyError("Checksum validation failed. Token is invalid!");
    }
    if (!input.equals(result.get(token))) {
      throw new VerifyError("Checksum validation failed. Invalid checksum!");
    }
  }

  /**
   * 
   * Calculates the checksum of the specified files.
   * Note: For performance reasons use the Adler32 implementation of the
   * java Checksum interface.
   * 
   * @see Adler32
   * @see Checksum
   * 
   * @param files List of files to be processed.
   * @return The checksum
   */
  public static String calculateChecksum(List<String> files) {
    Adler32 crc = new Adler32();
    for (String fileName : files) {
      try {
        InputStream in = BaseGame.class.getClassLoader().getResourceAsStream(fileName);
        if (in == null) {
          BaseGame.getLogger().log(
                  Level.SEVERE,
                  "InputStream could not be created for {0}",
                  fileName);
          throw new IllegalArgumentException("Error while trying to read " + fileName);
        }
        CheckedInputStream cin = new CheckedInputStream(in, crc);
        if (cin == null) {
          BaseGame.getLogger().log(
                  Level.SEVERE,
                  "InputStream could not be created for {0}",
                  fileName);
          throw new IllegalArgumentException("Error while trying to read " + fileName);
        }
        while (cin.read() != -1) {
          // Processing is done via CheckedInputStream
        }
        in.close();
      } catch (IOException ex) {
        Logger.getLogger(ChecksumManager.class.getName()).log(Level.SEVERE, null, ex);
        BaseGame.getLogger().log(
                Level.SEVERE,
                "Error during checksum calculation! {0}",
                ex.getMessage());
      }
    }
    return String.valueOf(crc.getValue());
  }
}
