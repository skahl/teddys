/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.protection;

import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ReqMessageSendChecksum;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cm
 */
public class ChecksumManagerThread extends Thread {
  
  @Override
  public void run() {
    //TODO dismiss old values
    for(;;) {
      //TODO Generate a new random token
      String token = String.valueOf(System.currentTimeMillis()/1000);
      ChecksumManager.result.put(token, "1");
      List<String> files = new ArrayList<String>();
      files.add("bla.class");
      ChecksumManager.files.put(token, files);
      // Send a message to the clients
      ReqMessageSendChecksum request = new ReqMessageSendChecksum(token, files);
      TeddyServer.getInstance().send(request);
      System.out.println("Checksum request sent.");
      // ... and sleep an amount of time.
      try {
        sleep(ChecksumManager.timerIntervall);
      } catch (InterruptedException ex) {
        Logger.getLogger(ChecksumManager.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
