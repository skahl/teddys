/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.BaseGame;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

/**
 *
 * @author cm
 */
public class ChecksumManager {

  private static ChecksumManagerThread thread;
  static Map<String, String> result = new HashMap<String, String>();
  static Map<String, List<String>> files = new HashMap<String, List<String>>();

  /**
   * Start the checksum manager timer. This sends checksum requests every intervall
   * to the clients of the server. Note: If all clients responded, use ready()
   * to generate a new list of files to be checked.
   */
  public static void startTimer() {
    if (thread != null) {
      return;
    }
    thread = new ChecksumManagerThread();
    thread.start();
    String tempMsg = String.format(
            "Checksum timer thread spawned (Rate: %f, Interval: %d ms)",
            (Float) (1f / GameSettings.CHECKSUM_INTERVAL * 1000),
            GameSettings.CHECKSUM_INTERVAL);
    MegaLogger.getLogger().debug(tempMsg);
  }

  public static void stopTimer() {
    if (thread == null || !thread.isAlive()) {
      return;
    }
    thread.stopThread();
    thread = null;
    MegaLogger.getLogger().debug("The checksum timer has been stopped.");
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
          MegaLogger.getLogger().error(new Throwable(
                  String.format("Input stream for the CRC calculation of %s is null!",
                  fileName)));
          throw new IllegalArgumentException("Error while trying to read " + fileName);
        }
        CheckedInputStream cin = new CheckedInputStream(in, crc);
        if (cin == null) {
          MegaLogger.getLogger().error(new Throwable(
                  String.format("Checked input stream for %s is null!", fileName)));
          throw new IllegalArgumentException("Error while trying to read " + fileName);
        }
        while (cin.read() != -1) {
          // Processing is done via CheckedInputStream
        }
        in.close();
      } catch (IOException ex) {
        MegaLogger.getLogger().fatal(new Throwable("Checksum calculation failed!", ex));
      }
    }
    return String.valueOf(crc.getValue());
  }
}
