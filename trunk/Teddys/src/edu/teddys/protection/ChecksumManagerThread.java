/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.protection;

import edu.teddys.BaseGame;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ReqMessageSendChecksum;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cm
 */
public class ChecksumManagerThread extends Thread {

  /**
   * 
   * Used for token generation.
   * 
   * @return A randomly generated string.
   */
  private String getRandomString() {
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
      files.add("bla.class");
      String result = ChecksumManager.calculateChecksum(files);
      // for every client, generate a token
      if (!TeddyServer.getInstance().getClientIDs().isEmpty()) {
        for (Integer clientID : TeddyServer.getInstance().getClientIDs()) {
          String token = getRandomString();
          BaseGame.getLogger().log(Level.INFO, "New token generated: {0}", token);
          ChecksumManager.files.put(token, files);
          ChecksumManager.result.put(token, result);
          // Send a message to the clients
          ReqMessageSendChecksum request = new ReqMessageSendChecksum(token, files, clientID);
          TeddyServer.getInstance().send(request);
          BaseGame.getLogger().info(String.format("Token request sent (Token: %s)", token));
        }
      }
      // ... and sleep an amount of time.
      try {
        sleep(ChecksumManager.timerIntervall);
      } catch (InterruptedException ex) {
        Logger.getLogger(ChecksumManager.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
