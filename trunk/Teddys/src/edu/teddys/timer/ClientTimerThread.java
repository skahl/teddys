/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.client.ManControllerInput;

/**
 *
 * @author cm
 */
public class ClientTimerThread extends Thread {

  @Override
  public void run() {

    for (;;) {
      ManControllerInput input = new ManControllerInput(ClientTimer.input);
      TeddyClient.getInstance().send(input);
      ClientTimer.input.clear();

      try {
        sleep(GameSettings.SERVER_TIMESTAMP_INTERVAL);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }
}
