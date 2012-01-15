/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.network.messages.server.ManMessageTransferServerData;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cm
 */
public class ServerDataSyncThread extends Thread {

  @Override
  public void run() {
    //TODO dismiss old values
    for (;;) {
      ManMessageTransferServerData msg = new ManMessageTransferServerData(TeddyServer.getInstance().getData());
      TeddyClient.getInstance().send(msg);
      // ... and sleep an amount of time.
      try {
        sleep(ServerDataSync.timerIntervall);
      } catch (InterruptedException ex) {
        Logger.getLogger(ServerDataSync.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
