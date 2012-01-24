/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ManMessageTransferServerData;

/**
 *
 * @author cm
 */
public class ServerDataSyncThread extends Thread {

  @Override
  public void run() {
    //TODO dismiss old values
    for (;;) {
      //TODO(nice to have) delta compression?
      ManMessageTransferServerData msg = new ManMessageTransferServerData(TeddyServer.getInstance().getData());
      TeddyClient.getInstance().send(msg);
      // ... and sleep an amount of time.
      try {
        sleep(GameSettings.SERVER_SYNC_INTERVAL);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }
}
