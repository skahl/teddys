/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ReqMessageSendChecksum;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author cm
 */
public class ChecksumManagerThread extends Thread {

  /**
   * 
   * Generate a new random string.
   * 
   * @return A randomly generated string.
   */
  private String getNewToken() {
    Random r = new Random();
    /*
     * Taken from http://bright-green.com/blog/2005_04_05/generating_a_random_string.html
     */
    // maximal length: ceil(ln(263) / ln(36)) = 13 chars
    return Long.toString(Math.abs(r.nextLong()), 36);
  }

  @Override
  public void run() {
    //TODO dismiss old values
    for (;;) {
      // TODO choose files randomly
      List<String> files = new ArrayList<String>();
      // Get the current working directory
//      String wd = System.getProperty("user.dir") + "/";
      files.add("/edu/teddys/BaseGame.class");
//      files.add("BaseGame.java");
      String result = ChecksumManager.calculateChecksum(files);
      // for every client, generate a token
      if (!TeddyServer.getInstance().getClientIDs().isEmpty()) {
        for (Integer clientID : TeddyServer.getInstance().getClientIDs()) {
          String token = getNewToken();
          ChecksumManager.files.put(token, files);
          ChecksumManager.result.put(token, result);
          // Send a message to the clients
          ReqMessageSendChecksum request = new ReqMessageSendChecksum(token, files, clientID);
          TeddyServer.getInstance().send(request);
          MegaLogger.getLogger().debug(
                  String.format("New token (%s) generated and sent. Expected checksum: %s", 
                  token, result)
                  );
        }
      }
      // ... and sleep an amount of time.
      try {
        sleep(GameSettings.CHECKSUM_INTERVAL);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }
}
